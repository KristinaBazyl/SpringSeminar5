package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Role;
import ru.gb.springdemo.model.User;
import ru.gb.springdemo.service.RoleService;
import ru.gb.springdemo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    //получить список всех ролей
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles()); }

    // создание роли
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role){
        return new ResponseEntity<>(roleService.addRole(role), HttpStatus.CREATED);
    }
}
