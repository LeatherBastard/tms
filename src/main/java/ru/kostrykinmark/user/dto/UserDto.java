package ru.kostrykinmark.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@Builder
public class UserDto {
    @NotBlank
    private String username;
    @Email
    private String email;
}
