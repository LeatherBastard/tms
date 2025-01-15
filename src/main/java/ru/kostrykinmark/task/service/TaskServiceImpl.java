package ru.kostrykinmark.task.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.exception.EntityNotFoundException;
import ru.kostrykinmark.exception.TaskRelatedCommentsException;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.UpdateTaskAdminRequest;
import ru.kostrykinmark.task.dto.UpdateTaskUserRequest;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.kostrykinmark.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;


@Service
public class TaskServiceImpl implements TaskService {

    public static final String TASK_NOT_FOUND_MESSAGE = "Task with id %d was not found";

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final TaskMapper taskMapper;

    private final EntityManager entityManager;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, CommentRepository commentRepository, TaskMapper taskMapper, EntityManager entityManager) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.taskMapper = taskMapper;
        this.entityManager = entityManager;
    }

    @Override
    public TaskFullDto addTask(int adminId, NewTaskDto newTaskDto) {
        Optional<User> optionalAuthor = userRepository.findById(adminId);
        if (optionalAuthor.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, adminId);
        Optional<User> optionalExecutor = userRepository.findById(newTaskDto.getExecutorId());
        if (optionalExecutor.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, newTaskDto.getExecutorId());
        Task task = taskMapper.mapToTask(newTaskDto);
        task.setStatus(TaskStatus.WAITING);
        task.setExecutor(optionalExecutor.get());
        return taskMapper.mapToTaskFullDto(taskRepository.save(task));
    }

    @Override
    public List<TaskFullDto> getAllTasks(Integer authorId, Integer executorId, String text, List<String> statuses, List<String> priorities, int from, int size) {
        Optional<User> optionalAuthor = userRepository.findById(authorId);
        if (optionalAuthor.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, authorId);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> root = criteriaQuery.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();

        if (authorId != null)
            predicates.add(criteriaBuilder.isTrue(root.get("author")).in(authorId));

        if (executorId != null)
            predicates.add(criteriaBuilder.isTrue(root.get("executor")).in(executorId));

        if (text != null) {
            Predicate likeInTitle = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + text.toLowerCase() + "%"
            );
            Predicate likeInDescription = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + text.toLowerCase() + "%"
            );
        }
        if (statuses != null)
            predicates.add(criteriaBuilder.isTrue(root.get("status").as(String.class).in(statuses)));

        if (priorities != null)
            predicates.add(criteriaBuilder.isTrue(root.get("priority").as(String.class).in(priorities)));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Task> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);
        return query.getResultList().stream().map(taskMapper::mapToTaskFullDto).collect(Collectors.toList());
    }


    @Override
    public TaskFullDto getById(int taskId) {
        return taskMapper
                .mapToTaskFullDto(taskRepository
                        .findById(taskId).orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE, taskId)));
    }

    @Override
    public void delete(int taskId) {
        getById(taskId);

        List<Comment> comments = commentRepository.findByTask_Id(taskId);
        if (comments.size() > 0)
            throw new TaskRelatedCommentsException(taskId);
        taskRepository.delete(taskRepository.findById(taskId).get());
    }

    @Override
    public TaskFullDto updateEventByAdmin(int taskId, UpdateTaskAdminRequest adminTaskRequest) {
        return null;
    }


    @Override
    public TaskFullDto updateTaskByUser(int userId, int taskId, UpdateTaskUserRequest userEventRequest) {
        return null;
    }
}
