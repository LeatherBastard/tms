package ru.kostrykinmark.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;
import ru.kostrykinmark.user.service.UserDetailsServiceImpl;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDetailsServiceImplTest {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;


    private User user;

    @BeforeEach
    void initalize() {
        user = User.builder()
                .username("kostrykinmark")
                .email("kostrykinmark@gmail.com")
                .password("12345")
                .roles(Set.of(roleRepository.findUserRole()))
                .build();
        userRepository.save(user);
    }

    @Test
    void test_loadUserByUsername_returnsUserWithCorrectEmail() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    void test_loadByUsernameWhenEmpty_throwsException() {
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("email"));
    }
}
