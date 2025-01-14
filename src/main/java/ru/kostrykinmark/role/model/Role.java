package ru.kostrykinmark.role.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@Data
@Entity
@Table(name = "roles", schema = "public")
public class Role implements GrantedAuthority {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "authority")
    private String authority;

}
