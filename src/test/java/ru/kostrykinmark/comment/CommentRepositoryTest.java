package ru.kostrykinmark.comment;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    private User user;
    private Task task;
    private Comment comment;
    private Comment comment2;


    @BeforeEach
    public void initialize() {
        user = User.builder()
                .username("kostrykinmark")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .roles(Set.of(roleRepository.findAdminRole()))
                .build();
        user = userRepository.save(user);
        task = Task.builder()
                .title("My Task adadadaddaddadadad")
                .description("My Task description dadasddsaadasdsadas")
                .priority(TaskPriority.HIGH)
                .author(user)
                .executor(user)
                .build();
        task = taskRepository.save(task);
        comment = Comment.builder()
                .user(user)
                .task(task)
                .text("My comment")
                .created(LocalDateTime.now())
                .build();
        comment = commentRepository.save(comment);
        comment2 = Comment.builder()
                .user(user)
                .task(task)
                .text("My comment #2")
                .created(LocalDateTime.now())
                .build();
        comment2 = commentRepository.save(comment2);

    }

    @Test
    public void whenFindByTaskId_thenReturnComments() {
        List<Comment> comments = commentRepository.findByTask_Id(task.getId());
        assertTrue(comments.size() == 2);
        assertEquals(user, comments.get(0).getUser());
        assertEquals(task, comments.get(0).getTask());
        assertEquals("My comment", comments.get(0).getText());
        assertEquals(user, comments.get(1).getUser());
        assertEquals(task, comments.get(1).getTask());
        assertEquals("My comment #2", comments.get(1).getText());
    }

    @Test
    public void whenNotFindByTaskId_thenReturnEmptyList() {
        List<Comment> comments = commentRepository.findByTask_Id(9);
        assertTrue(comments.isEmpty());
    }

}
