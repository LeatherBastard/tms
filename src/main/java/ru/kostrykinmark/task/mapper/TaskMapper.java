package ru.kostrykinmark.task.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.TaskShortDto;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.user.dto.UserShortDto;


@Component
public class TaskMapper {
    TaskShortDto mapToTaskShortDto(Task task) {
        return TaskShortDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserShortDto.builder()
                        .id(task.getAuthor().getId())
                        .username(task.getAuthor().getUsername())
                        .build())
                .executor(UserShortDto.builder()
                        .id(task.getExecutor().getId())
                        .username(task.getExecutor().getUsername())
                        .build())
                .build();
    }

    public TaskFullDto mapToTaskFullDto(Task task) {
        return TaskFullDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserShortDto.builder()
                        .id(task.getAuthor().getId())
                        .username(task.getAuthor().getUsername())
                        .build())
                .executor(UserShortDto.builder()
                        .id(task.getExecutor().getId())
                        .username(task.getExecutor().getUsername())
                        .build())
                .build();
    }

    public Task mapToTask(NewTaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .priority(taskDto.getPriority())
                .build();
    }


}
