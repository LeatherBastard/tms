package ru.kostrykinmark.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kostrykinmark.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

}
