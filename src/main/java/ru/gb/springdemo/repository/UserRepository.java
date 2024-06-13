package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // получить пользователя по логину
    @Query("SELECT u FROM User u WHERE u.login = :login")
    Optional<User> findByLogin(String login);

}
