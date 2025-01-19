package ru.kostrykinmark.comment;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.comment.dto.NewCommentDto;
import ru.kostrykinmark.comment.mapper.CommentMapper;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.comment.service.CommentServiceImpl;
import ru.kostrykinmark.exception.EntityNotFoundException;
import ru.kostrykinmark.exception.TaskAuthorException;
import ru.kostrykinmark.role.model.Role;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.mapper.UserMapper;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CommentServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private EntityManager entityManager;

    private UserMapper userMapper;
    private TaskMapper taskMapper;
    private CommentMapper commentMapper;


    private CommentServiceImpl commentService;

    @Captor
    ArgumentCaptor<Comment> commentArgumentCaptor;

    private Role role;
    private Task task;
    private User user;
    private User secondUser;
    private Comment comment;
    private NewCommentDto commentDto;

    @BeforeEach
    public void initialize() {
        userMapper = new UserMapper();
        commentMapper = new CommentMapper();

        commentService = new CommentServiceImpl(roleRepository, commentRepository, userRepository, taskRepository
                , commentMapper, userMapper, taskMapper, entityManager);

        role = Role.builder()
                .id(1)
                .name("ROLE_USER")
                .build();
        user = User.builder()
                .id(1)
                .username("Mark")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .roles(Set.of(role))
                .build();
        secondUser = User.builder()
                .id(2)
                .username("Mark")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .roles(Set.of(role))
                .build();
        task = Task.builder()
                .id(1)
                .title("Some titleeeeeeeee")
                .description("Some descriptionnnnnnnnnnnn")
                .status(TaskStatus.IN_PROCESS)
                .priority(TaskPriority.LOW)
                .author(user)
                .executor(user)
                .build();
        comment = Comment.builder()
                .id(0)
                .task(task)
                .user(user)
                .text("Some test")
                .created(LocalDateTime.now())
                .build();

        commentDto = NewCommentDto.builder()
                .text(comment.getText())
                .build();
    }


    @Test
    void whenAdd_thenReturnComment() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(roleRepository.findUserRole()).thenReturn(role);
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any())).thenReturn(comment);
        commentService.add(1, 1, commentDto);
        verify(commentRepository).save(commentArgumentCaptor.capture());
        Comment actualComment = commentArgumentCaptor.getValue();
        assertEquals(comment.getId(), actualComment.getId());
        assertEquals(comment.getText(), actualComment.getText());
        assertEquals(comment.getUser(), actualComment.getUser());
        assertEquals(comment.getTask(), actualComment.getTask());

    }

    @Test
    void whenAddUserNotFound_thenEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> commentService.add(1, 1, commentDto));
    }

    @Test
    void whenAddTaskNotFound_thenEntityNotFoundException() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        assertThrows(EntityNotFoundException.class, () -> commentService.add(1, 1, commentDto));
    }

    @Test
    void whenAddAuthorIsNotUser_thenTaskAuthorException() {
        when(userRepository.findById(any())).thenReturn(Optional.of(secondUser));
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(roleRepository.findUserRole()).thenReturn(role);
        assertThrows(TaskAuthorException.class, () -> commentService.add(1, 1, commentDto));
    }
}
