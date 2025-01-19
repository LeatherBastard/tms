package ru.kostrykinmark.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInUserDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
