package ru.kostrykinmark.task.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostrykinmark.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

}
