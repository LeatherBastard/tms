package ru.kostrykinmark.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kostrykinmark.role.model.Role;

import java.lang.annotation.Native;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(
            value = "SELECT * FROM roles WHERE authority = 'ROLE_USER'",
            nativeQuery = true)
    Role findUserRole();

    @Query(
            value = "SELECT * FROM roles WHERE authority = 'ROLE_ADMIN'",
            nativeQuery = true)
    Role findAdminRole();
}
