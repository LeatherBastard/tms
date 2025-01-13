package ru.kostrykinmark.user.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;




@Builder
@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username", nullable = false, length = 250)
    @Size(min = 2, max = 250)
    private String username;
    @Column(name = "email", nullable = false, length = 254)
    @Size(min = 6, max = 254)
    private String email;
    @Column(name = "password", nullable = false, length = 50)
    @Size(min = 6, max = 50)
    private String password;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;
}
