package ru.kostrykinmark.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.exception.UserEmailOccupiedException;
import ru.kostrykinmark.exception.UsernameOccupiedException;
import ru.kostrykinmark.jwtconfig.JwsUtils;
import ru.kostrykinmark.role.model.Role;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;
import ru.kostrykinmark.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwsUtils jwsUtils;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private static SignUpUserDto userSignUpDto;
    private static SignInUserDto userSignInDto;

    @BeforeAll
    public static void initDto() {
        userSignUpDto = SignUpUserDto.builder()
                .username("Mark")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .build();
        userSignInDto = SignInUserDto.builder()
                .username("Mark")
                .password("12345")
                .build();
    }

    @BeforeEach
    public void initialize() {
        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                passwordEncoder,
                authenticationManager,
                jwsUtils);
    }

    @Test
    public void whenSignUpUser_thenSuccessful() {
        when(userRepository.existsUserByUsername(userSignUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsUserByEmail(userSignUpDto.getEmail())).thenReturn(false);
        when(roleRepository.findUserRole()).thenReturn(Role.builder()
                .id(2)
                .name("ROLE_USER")
                .build());
        when(passwordEncoder.encode(anyString())).thenReturn("12345");
        userService.signUpUser(userSignUpDto);
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertEquals(userSignUpDto.getUsername(), savedUser.getUsername());
        assertEquals(userSignUpDto.getEmail(), savedUser.getEmail());
        assertEquals(userSignUpDto.getPassword(), savedUser.getPassword());
    }

    @Test
    public void whenSignUpUsernameOccupied_thenUsernameOccupiedException() {
        when(userRepository.existsUserByUsername(userSignUpDto.getUsername())).thenReturn(true);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(passwordEncoder);
        assertThrows(UsernameOccupiedException.class, () -> userService.signUpUser(userSignUpDto));
    }

    @Test
    public void whenSignUpUserEmailOccupied_thenUserEmailOccupiedException() {
        when(userRepository.existsUserByUsername(userSignUpDto.getEmail())).thenReturn(false);
        when(userRepository.existsUserByEmail(userSignUpDto.getEmail())).thenReturn(true);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(passwordEncoder);
        assertThrows(UserEmailOccupiedException.class, () -> userService.signUpUser(userSignUpDto));
    }

    @Test
    public void whenSignInUser_thenSuccessful() {
        userService.signInUser(userSignInDto);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwsUtils, times(1)).generateJwtToken(any());
    }

}
