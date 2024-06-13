package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.User;
import ru.gb.springdemo.service.BookService;
import ru.gb.springdemo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name= "Users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // получить пользователя по id
    @Operation(summary = "get user by ID", description = "Поиск пользователя по ID пользователя")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // получить пользователя по логину
    @Operation(summary = "get user by login", description = "Поиск пользователя по логину пользователя")
    @GetMapping("/login/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        try {
            return ResponseEntity.ok(userService.getUserByLogin(login));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //получить список всех пользователей
    @Operation(summary = "get all users", description = "Поиск всех пользователей в системе")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers()); }

    // создание пользователя
    @Operation(summary = "create user", description = "Добавить пользователя в общий список системы")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user){
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }
}
