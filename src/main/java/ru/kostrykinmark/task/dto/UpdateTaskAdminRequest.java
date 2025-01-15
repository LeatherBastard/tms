package ru.kostrykinmark.task.dto;


import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;

@Data
@Builder
public class UpdateTaskAdminRequest {
    @Size(min=3,max = 250)
    private String title;
    @Size(min=20,max = 2000)
    private String description;
    private String status;
    private String priority;
    private Integer executor;
}
