package com.example.gestion_bibliotheque.controller.user;

import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")  // endpoint de base
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users : récupère la liste de tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Long roleId) {
        List<User> users = userService.getUsersByRoleId(roleId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
