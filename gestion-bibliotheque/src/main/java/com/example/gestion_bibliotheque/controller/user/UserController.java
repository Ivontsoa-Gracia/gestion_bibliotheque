package com.example.gestion_bibliotheque.controller.user;

import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.gestion_bibliotheque.dto.user.UserDTO;

@RestController
@RequestMapping("/api/users") 
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers()
            .stream()
            .map(userService::mapToDTO) 
            .toList();
    
        return ResponseEntity.ok(userDTOs);
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

    @GetMapping("/{id}/data")
    public ResponseEntity<UserDTO> getUserData(@PathVariable Long id) {
        UserDTO dto = userService.getDataUserById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build(); 
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

}
