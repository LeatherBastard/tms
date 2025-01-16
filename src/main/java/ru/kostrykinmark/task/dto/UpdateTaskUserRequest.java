package ru.kostrykinmark.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskUserRequest {
    @NotBlank
    private String status;
}
