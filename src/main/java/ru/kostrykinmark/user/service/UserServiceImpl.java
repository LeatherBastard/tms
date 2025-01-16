package ru.kostrykinmark.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kostrykinmark.exception.UserEmailOccupiedException;
import ru.kostrykinmark.exception.UsernameOccupiedException;
import ru.kostrykinmark.jwtconfig.JwsUtils;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwsUtils jwsUtils;


    @Override
    public void signUpAdmin(SignUpUserDto userDto) {
        if (userRepository.existsUserByUsername(userDto.getUsername()))
            throw new UsernameOccupiedException("Choose different name");
        if (userRepository.existsUserByEmail(userDto.getEmail()))
            throw new UserEmailOccupiedException("Choose different email");
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findAdminRole()));
        userRepository.save(user);
    }

    @Override
    public void signUpUser(SignUpUserDto userDto) {
        if (userRepository.existsUserByUsername(userDto.getUsername()))
            throw new UsernameOccupiedException("Choose different name");
        if (userRepository.existsUserByEmail(userDto.getEmail()))
            throw new UserEmailOccupiedException("Choose different email");
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findUserRole()));
        userRepository.save(user);
    }


    @Override
    public String signInUser(SignInUserDto user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwsUtils.generateJwtToken(authentication);
        return jwtToken;
    }


}
