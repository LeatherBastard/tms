package ru.kostrykinmark.comment.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.dto.NewCommentDto;
import ru.kostrykinmark.comment.mapper.CommentMapper;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.exception.EntityNotFoundException;
import ru.kostrykinmark.exception.TaskAuthorException;
import ru.kostrykinmark.exception.WrongDateRangeException;
import ru.kostrykinmark.role.model.Role;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.mapper.UserMapper;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.kostrykinmark.task.service.TaskServiceImpl.TASK_NOT_FOUND_MESSAGE;
import static ru.kostrykinmark.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String CREATED_FIELD_NAME = "created";

    private final RoleRepository roleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    private final EntityManager entityManager;

    @Override
    public CommentResponseDto add(int userId, int taskId, NewCommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE, taskId));
        Role userRole = roleRepository.findUserRole();
        if (user.getRoles().contains(userRole) && task.getAuthor().getId() != user.getId())
            throw new TaskAuthorException(taskId, userId);

        Comment comment = Comment.builder()
                .task(task)
                .user(user)
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .build();
        return commentMapper.mapToCommentDto(commentRepository.save(comment));
    }


    @Override
    public List<CommentResponseDto> getAllComments(String text, List<Integer> tasks, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd))
                throw new WrongDateRangeException(rangeStart, rangeEnd);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);

        List<Predicate> predicates = new ArrayList<>();
        if (text != null) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("text")),
                    "%" + text.toLowerCase() + "%")
            );
        }
        if (tasks != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("task").in(tasks)));
        }

        if (rangeStart != null && rangeEnd != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_FIELD_NAME), rangeStart));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_FIELD_NAME), rangeEnd));
        } else {
            predicates.add(criteriaBuilder.greaterThan(root.get(CREATED_FIELD_NAME), LocalDateTime.now().minusDays(1)));
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Comment> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);
        List<Comment> comments = query.getResultList();
        return setUserAndTaskToCommentDto(comments);
    }

    private List<CommentResponseDto> setUserAndTaskToCommentDto(List<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto commentDto = commentMapper.mapToCommentDto(comment);
            commentDto.setUser(userMapper.mapToUserShortDto(comment.getUser()));
            commentDto.setTask(taskMapper.mapToTaskShortDto(comment.getTask()));
            result.add(commentDto);
        }
        return result;
    }


}
