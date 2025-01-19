package ru.kostrykinmark.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.role.model.Role;
import ru.kostrykinmark.role.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void whenFindUserRole_thenReturnUserRole() {
        String roleName = "ROLE_USER";
        Role actual = roleRepository.findUserRole();
        assertEquals(roleName, actual.getName());
    }

    @Test
    void whenFindAdminRole_thenReturnAdminRole() {
        String roleName = "ROLE_ADMIN";
        Role actual = roleRepository.findAdminRole();
        assertEquals(roleName, actual.getName());
    }
}
