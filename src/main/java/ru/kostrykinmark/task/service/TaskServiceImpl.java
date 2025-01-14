package ru.kostrykinmark.task.service;

import io.jsonwebtoken.security.Jwks;
import org.springframework.stereotype.Service;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task addTask(NewTaskDto newTaskDto) {
        Task task = new Task();
        task.setAnnotation(newTaskDto.getAnnotation());
        task.setDescription(newTaskDto.getDescription());
        task.setStatus(newTaskDto.getStatus());
        task.setPriority(newTaskDto.getPriority());
        Optional<User> author=userRepository.findById(newTaskDto.getAuthorId());
        task.setAuthor(author.get());
        Optional<User> executor=userRepository.findById(newTaskDto.getExecutorId());
        task.setExecutor(executor.get());
       return taskRepository.save(task);
    }
}
