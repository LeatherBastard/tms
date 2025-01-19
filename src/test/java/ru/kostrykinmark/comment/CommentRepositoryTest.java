package ru.kostrykinmark.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CommentRepositoryTest {
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
        Comment comment = Comment.builder()
                .user(user)
                .task(task)
                .text("My comment")
                .created(LocalDateTime.now())
                .build();
        comment = commentRepository.save(comment);
        Comment comment2 = Comment.builder()
                .user(user)
                .task(task)
                .text("My comment #2")
                .created(LocalDateTime.now())
                .build();
        comment2 = commentRepository.save(comment2);

    }

    @Test
    void whenFindByTaskId_thenReturnComments() {
        List<Comment> comments = commentRepository.findByTask_Id(task.getId());
        assertEquals(2, comments.size());
        assertEquals(user, comments.get(0).getUser());
        assertEquals(task, comments.get(0).getTask());
        assertEquals("My comment", comments.get(0).getText());
        assertEquals(user, comments.get(1).getUser());
        assertEquals(task, comments.get(1).getTask());
        assertEquals("My comment #2", comments.get(1).getText());
    }

    @Test
    void whenNotFindByTaskId_thenReturnEmptyList() {
        List<Comment> comments = commentRepository.findByTask_Id(9);
        assertTrue(comments.isEmpty());
    }

}
