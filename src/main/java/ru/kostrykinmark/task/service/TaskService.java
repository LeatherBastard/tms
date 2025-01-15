package ru.kostrykinmark.task.service;

import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.UpdateTaskAdminRequest;
import ru.kostrykinmark.task.dto.UpdateTaskUserRequest;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;

import java.util.List;

public interface TaskService {

    List<TaskFullDto> getAllTasks(Integer authorId, Integer executorId, String text, List<String> statuses, List<String> priorities, int from, int size);

    TaskFullDto getById(int taskId);

    void delete(int taskId);

    TaskFullDto updateEventByAdmin(int taskId, UpdateTaskAdminRequest adminTaskRequest);

    TaskFullDto updateTaskByUser(int userId, int taskId, UpdateTaskUserRequest userEventRequest);

    TaskFullDto addTask(int adminId, NewTaskDto taskDto);
}
