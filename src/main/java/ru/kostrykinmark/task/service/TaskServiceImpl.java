package ru.kostrykinmark.task.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.exception.*;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.UpdateTaskAdminRequest;
import ru.kostrykinmark.task.dto.UpdateTaskUserRequest;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kostrykinmark.task.model.TaskStatus.*;
import static ru.kostrykinmark.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    public static final String TASK_NOT_FOUND_MESSAGE = "Task with id %d was not found";
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final TaskMapper taskMapper;
    private final EntityManager entityManager;

    @Override
    public TaskFullDto addTask(int adminId, NewTaskDto newTaskDto) {
        User author = userRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, adminId));
        User executor = userRepository.findById(newTaskDto.getExecutorId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, newTaskDto.getExecutorId()));
        Task task = taskMapper.mapToTask(newTaskDto);
        task.setAuthor(author);
        task.setExecutor(executor);
        try {
            task.setPriority(TaskPriority.valueOf(newTaskDto.getPriority()));
        } catch (IllegalArgumentException e) {
            throw new TaskIncorrectPriorityException(newTaskDto.getPriority());
        }
        task.setStatus(WAITING);
        return taskMapper.mapToTaskFullDto(taskRepository.save(task));
    }

    @Override
    public List<TaskFullDto> getAllTasks(Integer authorId, Integer executorId, String text, List<String> statuses, List<String> priorities, int from, int size) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, authorId));
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
        return taskMapper.mapToTaskFullDto(getTaskById(taskId));
    }

    @Override
    public void delete(int taskId) {
        Task task = getTaskById(taskId);
        List<Comment> comments = commentRepository.findByTask_Id(taskId);
        if (comments.size() > 0)
            throw new TaskRelatedCommentsException(taskId);
        taskRepository.delete(task);
    }

    @Override
    public TaskFullDto updateEventByAdmin(int taskId, UpdateTaskAdminRequest adminTaskRequest) {
        Task oldTask = getTaskById(taskId);
        if (adminTaskRequest.getTitle() != null)
            oldTask.setTitle(adminTaskRequest.getTitle());
        if (adminTaskRequest.getDescription() != null)
            oldTask.setDescription(adminTaskRequest.getDescription());

        if (adminTaskRequest.getStatus() != null) {
            String status = adminTaskRequest.getStatus();
            try {
                oldTask.setStatus(TaskStatus.valueOf(status));
            } catch (IllegalArgumentException e) {
                throw new TaskIncorrectStatusException(status);
            }
        }

        if (adminTaskRequest.getPriority() != null) {
            String priority = adminTaskRequest.getStatus();
            try {
                oldTask.setPriority(TaskPriority.valueOf(priority));
            } catch (IllegalArgumentException e) {
                throw new TaskIncorrectPriorityException(priority);
            }
        }

        if (adminTaskRequest.getExecutor() != null) {
            int executorId = adminTaskRequest.getExecutor();
            User executor = userRepository.findById(executorId)
                    .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, executorId));
            oldTask.setExecutor(executor);
        }

        return taskMapper.mapToTaskFullDto(taskRepository.save(oldTask));
    }

    @Override
    public TaskFullDto updateTaskByUser(int userId, int taskId, UpdateTaskUserRequest userEventRequest) {
        Task oldTask = getTaskById(taskId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId));
        if (oldTask.getAuthor().getId() != user.getId()) {
            throw new TaskAuthorException(taskId, userId);
        }
        if (userEventRequest.getStatus() != null) {
            String status = userEventRequest.getStatus();
            if (status.equals(WAITING.name()))
                oldTask.setStatus(WAITING);
            else if (status.equals(IN_PROCESS.name()))
                oldTask.setStatus(IN_PROCESS);
            else if ((status.equals(FINISHED.name())))
                oldTask.setStatus(FINISHED);
            else throw new TaskIncorrectStatusException(status);
        }
        return taskMapper.mapToTaskFullDto(taskRepository.save(oldTask));
    }

    private Task getTaskById(int taskId) {
        return taskRepository
                .findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE, taskId));
    }
}
