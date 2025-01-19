package ru.kostrykinmark.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.task.service.TaskService;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TaskServiceIntegrationTest {
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Test
    void getAllTasks() {
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
        userRepository.save(user);
        TaskFullDto firstTaskDto = taskMapper.mapToTaskFullDto(taskRepository.save(task));
        TaskFullDto secondTaskDto = taskMapper.mapToTaskFullDto(taskRepository.save(secondTask));
        List<TaskFullDto> tasks = taskService.getAllTasks(
                user.getId(), user.getId(), null, null, List.of(TaskPriority.MEDIUM.name()), 0, 10);
        assertEquals(2, tasks.size());
        assertEquals(firstTaskDto, tasks.get(0));
        assertEquals(secondTaskDto, tasks.get(1));
    }
}
