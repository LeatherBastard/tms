package ru.kostrykinmark.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwsUtils jwsUtils;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwsUtils jwsUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwsUtils = jwsUtils;
    }



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
        user.setAuthorities(Set.of(roleRepository.findAdminRole()));
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
        user.setAuthorities(Set.of(roleRepository.findUserRole()));
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
