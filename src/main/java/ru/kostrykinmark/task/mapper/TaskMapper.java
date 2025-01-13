package ru.kostrykinmark.task.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.task.dto.TaskDto;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.user.dto.UserDto;



@Component
public class TaskMapper {
    public TaskDto mapToTaskDto(Task task) {
        return TaskDto.builder()
                .annotation(task.getAnnotation())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(
                        UserDto.builder()
                                .username(task.getAuthor().getUsername())
                                .email(task.getAuthor().getEmail())
                                .build()

                )
                .executor(

                        UserDto.builder()
                                .username(task.getExecutor().getUsername())
                                .email(task.getExecutor().getEmail())
                                .build()
                )
                .build();
    }

    public Task mapToTask(TaskDto taskDto) {
        return Task.builder()
                .annotation(taskDto.getAnnotation())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .priority(taskDto.getPriority())
                .build();
    }
}
