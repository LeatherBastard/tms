package ru.kostrykinmark.task.dto;


import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskAdminRequest {
    @Size(min = 5, max = 250)
    private String title;
    @Size(min = 20, max = 2000)
    private String description;
    private String status;
    private String priority;
    private Integer executor;
}
