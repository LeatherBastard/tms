package ru.kostrykinmark.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;
import ru.kostrykinmark.user.service.UserService;

@RestController
@RequestMapping(path = "/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpUser(@RequestBody SignUpUserDto user) {
        userService.signUpAdmin(user);
    }


    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    public String signInUser(@RequestBody SignInUserDto user) {
        return userService.signInUser(user);
    }


}
