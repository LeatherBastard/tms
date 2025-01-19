package ru.kostrykinmark.comment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.mapper.CommentMapper;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentServiceIntegrationTest {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Test
    void getAllComments() {
        User user = User.builder().username("kostrykinmark").email("kostrykinmark@gmail.com").password("12345").build();
        Task task = Task.builder()
                .title("Some taskkkkkkkkkkkk")
                .description("Some descriptionnnnnnnnn").
                priority(TaskPriority.MEDIUM)
                .author(user)
                .executor(user)
                .build();

        Task secondTask = Task.builder()
                .title("Second taskkkkkkkkkkkk")
                .description("Some descriptionnnnnnnnn").
                priority(TaskPriority.MEDIUM)
                .author(user)
                .executor(user)
                .build();

        Comment comment = Comment.builder()
                .text("This is a first comment")
                .task(task)
                .user(user)
                .created(LocalDateTime.now())
                .build();

        Comment secondComment = Comment.builder()
                .text("This is a second comment")
                .task(secondTask)
                .user(user)
                .created(LocalDateTime.now())
                .build();

        userRepository.save(user);
        taskRepository.save(task);
        taskRepository.save(secondTask);
        CommentResponseDto commentResponseDto = commentMapper.mapToCommentDto(commentRepository.save(comment));
        CommentResponseDto secondCommentResponseDto = commentMapper.mapToCommentDto(commentRepository.save(secondComment));

        List<CommentResponseDto> comments = commentService.getAllComments(
                "SECOND", null, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 0, 10);
        assertEquals(1, comments.size());

        assertEquals(secondCommentResponseDto, comments.get(0));
    }
}
