package ru.kostrykinmark.task.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.task.dto.TaskDto;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.user.dto.UserDto;

import javax.persistence.Column;

@Component
public class TaskMapper {
    public TaskDto mapToTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .annotation(task.getAnnotation())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(
                        UserDto.builder()
                                .id(task.getAuthor().getId())
                                .name(task.getAuthor().getName())
                                .email(task.getAuthor().getEmail())
                                .build()

                )
                .build();
    }

    public Task mapToTask(TaskDto taskDto) {
        return Task.builder().build();
    }
}
