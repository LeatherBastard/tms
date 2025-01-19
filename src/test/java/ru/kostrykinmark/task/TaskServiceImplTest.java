package ru.kostrykinmark.task;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.exception.EntityNotFoundException;
import ru.kostrykinmark.exception.TaskIncorrectPriorityException;
import ru.kostrykinmark.exception.TaskIncorrectStatusException;
import ru.kostrykinmark.exception.TaskRelatedCommentsException;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.dto.UpdateTaskAdminRequest;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.task.service.TaskServiceImpl;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    private TaskMapper taskMapper;
    @Mock
    private EntityManager entityManager;


    TaskServiceImpl taskService;

    @Captor
    ArgumentCaptor<Task> taskArgumentCaptor;

    private UpdateTaskAdminRequest updateTaskAdmin;
    private UpdateTaskAdminRequest notFullUpdateTaskAdmin;
    private NewTaskDto taskDto;
    private Task task;
    private Task secondTask;
    private User user;
    private User secondUser;


    @BeforeEach
    public void initialize() {
        taskMapper = new TaskMapper();
        taskService = new TaskServiceImpl(taskRepository, userRepository, commentRepository, taskMapper, entityManager);

        user = User.builder()
                .id(1)
                .username("Mark")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .build();
        secondUser = User.builder()
                .id(2)
                .username("new executor")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .build();
        taskDto = NewTaskDto.builder()
                .title("Some titleeeeeee")
                .description("Some description............")
                .priority(TaskPriority.HIGH.name())
                .executorId(user.getId())
                .build();
        task = Task.builder()
                .id(1)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(TaskStatus.IN_PROCESS)
                .priority(TaskPriority.valueOf(taskDto.getPriority()))
                .author(user)
                .executor(user)
                .build();

        secondTask = Task.builder()
                .id(2)
                .title("Second task title")
                .description("Second tassssssssssssk")
                .status(TaskStatus.IN_PROCESS)
                .priority(TaskPriority.MEDIUM)
                .author(user)
                .executor(user)
                .build();

        updateTaskAdmin = UpdateTaskAdminRequest.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(TaskStatus.FINISHED.name())
                .priority(TaskPriority.LOW.name())
                .executor(task.getExecutor().getId())
                .build();

        notFullUpdateTaskAdmin = UpdateTaskAdminRequest.builder()
                .status(TaskStatus.FINISHED.name())
                .executor(task.getExecutor().getId())
                .build();
    }

    @Nested
    class addTaskTests {
        @Test
        void whenAddTask_thenReturnTask() {
            when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
            when(taskRepository.save(any())).thenReturn(task);
            taskService.addTask(user.getId(), taskDto);
            verify(taskRepository).save(taskArgumentCaptor.capture());
            Task actualTask = taskArgumentCaptor.getValue();
            assertEquals(taskDto.getTitle(), actualTask.getTitle());
            assertEquals(taskDto.getDescription(), actualTask.getDescription());
            assertEquals(taskDto.getPriority(), actualTask.getPriority().name());
            assertEquals(TaskStatus.WAITING, actualTask.getStatus());
            assertEquals(user, actualTask.getAuthor());
            assertEquals(user, actualTask.getAuthor());
        }

        @Test
        void whenAddTaskAuthorOrExecutorNotFound_thenReturnEntityNotFoundException() {
            when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> taskService.addTask(1, taskDto));
            verifyNoInteractions(taskRepository);
        }

        @Test
        void whenAddTaskIncorrectPriority_thenReturnTaskIncorrectPriorityException() {
            taskDto.setPriority("1234");
            assertThrows(EntityNotFoundException.class, () -> taskService.addTask(1, taskDto));
            verifyNoInteractions(taskRepository);
        }
    }

    @Nested
    class updateTaskByAdminTests {
        @Test
        void whenUpdateTaskByAdmin_thenReturnUpdatedTask() {
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            when(userRepository.findById(updateTaskAdmin.getExecutor())).thenReturn(Optional.of(secondUser));
            when(taskRepository.save(any())).thenReturn(task);
            taskService.updateTaskByAdmin(1, updateTaskAdmin);
            verify(taskRepository).save(taskArgumentCaptor.capture());
            Task actualTask = taskArgumentCaptor.getValue();
            assertEquals(updateTaskAdmin.getTitle(), actualTask.getTitle());
            assertEquals(updateTaskAdmin.getDescription(), actualTask.getDescription());
            assertEquals(updateTaskAdmin.getPriority(), actualTask.getPriority().name());
            assertEquals(updateTaskAdmin.getStatus(), actualTask.getStatus().name());
            assertEquals(updateTaskAdmin.getPriority(), actualTask.getPriority().name());
            assertEquals(secondUser.getId(), actualTask.getExecutor().getId());
        }

        @Test
        void whenUpdateTaskByAdminWithSomeFieldsEmpty_thenReturnUpdatedTask() {
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            when(userRepository.findById(notFullUpdateTaskAdmin.getExecutor())).thenReturn(Optional.of(secondUser));
            when(taskRepository.save(any())).thenReturn(task);
            taskService.updateTaskByAdmin(1, notFullUpdateTaskAdmin);
            verify(taskRepository).save(taskArgumentCaptor.capture());
            Task actualTask = taskArgumentCaptor.getValue();
            assertEquals(notFullUpdateTaskAdmin.getStatus(), actualTask.getStatus().name());
            assertEquals(secondUser.getId(), actualTask.getExecutor().getId());
        }

        @Test
        void whenUpdateTaskByAdminIncorrectPriority_thenReturnTaskIncorrectPriorityException() {
            updateTaskAdmin.setPriority("1234");
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            assertThrows(TaskIncorrectPriorityException.class, () -> taskService.updateTaskByAdmin(1, updateTaskAdmin));
        }

        @Test
        void whenUpdateTaskByAdminIncorrectStatus_thenReturnTaskIncorrectStatusException() {
            updateTaskAdmin.setStatus("1234");
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            assertThrows(TaskIncorrectStatusException.class, () -> taskService.updateTaskByAdmin(1, updateTaskAdmin));
        }

        @Test
        void whenUpdateTaskByAdminExecutorNotFound_thenReturnEntityNotFoundException() {
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            assertThrows(EntityNotFoundException.class, () -> taskService.updateTaskByAdmin(1, updateTaskAdmin));
        }
    }

    @Nested
    class deleteTests {
        @Test
        void whenDelete_thenDeleteTask() {
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            task.setComments(List.of());
            taskService.delete(1);
            verify(taskRepository, times(1)).delete(task);
        }

        @Test
        void whenDeleteTaskHasComments_thenTaskRelatedCommentsException() {
            when(taskRepository.findById(any())).thenReturn(Optional.of(task));
            task.setComments(List.of(Comment.builder().build()));
            assertThrows(TaskRelatedCommentsException.class, () -> taskService.delete(1));

        }

    }


}
