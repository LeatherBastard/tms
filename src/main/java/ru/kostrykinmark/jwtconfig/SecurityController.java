package ru.kostrykinmark.jwtconfig;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kostrykinmark.user.service.UserService;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwsUtils jwsUtils;

    public SecurityController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwsUtils jwsUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwsUtils = jwsUtils;
    }

    @PostMapping("/signin")


    @PostMapping("/signup")
}
