package ru.kostrykinmark.task.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.model.Task;


@Component
public class TaskMapper {
    public NewTaskDto mapToTaskDto(Task task) {
        return NewTaskDto.builder()
                .build();
    }


}
