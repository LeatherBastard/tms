package ru.kostrykinmark.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NewTaskDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String priority;
    @NotNull
    private Integer executorId;

}
