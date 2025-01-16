package ru.kostrykinmark.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kostrykinmark.role.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(
            value = "SELECT * FROM roles WHERE name = 'ROLE_USER'",
            nativeQuery = true)
    Role findUserRole();

    @Query(
            value = "SELECT * FROM roles WHERE name = 'ROLE_ADMIN'",
            nativeQuery = true)
    Role findAdminRole();
}
