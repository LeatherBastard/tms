package ru.kostrykinmark.user;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.user.model.User;
import ru.kostrykinmark.user.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;

    @BeforeEach
    public void initialize() {
        user = User.builder()
                .username("Mark")
                .password("1234")
                .email("kostrykinmark@gmail.com")
                .roles(Set.of(roleRepository.findAdminRole()))
                .build();
        userRepository.save(user);
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        Optional<User> actual = userRepository.findByUsername(user.getUsername());
        assertTrue(actual.isPresent());
        User actualUser = actual.get();
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertEquals(user.getPassword(), actualUser.getPassword());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getRoles(), actualUser.getRoles());
    }

    @Test
    public void whenNotFindByUsername_thenReturnEmpty() {
        Optional<User> actual = userRepository.findByUsername("username");
        assertTrue(actual.isEmpty());
    }

    @Test
    public void whenExistsByUsername_thenReturnTrue() {
        boolean actual = userRepository.existsUserByUsername(user.getUsername());
        assertTrue(actual);
    }

    @Test
    public void whenNotExistsByUsername_thenReturnFalse() {
        boolean actual = userRepository.existsUserByUsername("adadadda");
        assertFalse(actual);
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        boolean actual = userRepository.existsUserByEmail(user.getEmail());
        assertTrue(actual);
    }

    @Test
    public void whenNotExistsByEmail_thenReturnFalse() {
        boolean actual = userRepository.existsUserByEmail("adsad@yandex.ru");
        assertFalse(actual);
    }

}
