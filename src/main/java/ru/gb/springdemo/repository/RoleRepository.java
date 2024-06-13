package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.springdemo.model.Role;
import ru.gb.springdemo.model.User;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
