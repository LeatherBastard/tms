package ru.kostrykinmark.user.service;

import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;

public interface UserService {
    void signUpUser(SignUpUserDto user);
    String signInUser(SignInUserDto user);
}
