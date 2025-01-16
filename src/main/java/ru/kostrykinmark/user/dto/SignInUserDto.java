package ru.kostrykinmark.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInUserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
