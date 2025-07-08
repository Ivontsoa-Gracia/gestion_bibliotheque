package com.example.gestion_bibliotheque.controller.user;

import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.gestion_bibliotheque.entity.user.Role;
import com.example.gestion_bibliotheque.repository.user.RoleRepository;
import com.example.gestion_bibliotheque.dto.user.UserDTO;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    
    public static class LoginRequest {
        public String email;
        public String password;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email, request.password)
            );
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Connexion réussie");
    
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }
    

    public static class RegisterRequest {
        public String name;
        public String email;
        public String username;
        public String password;
        public String profile; 
        public String roleName; 
    }

    // @PostMapping(value = "/register", consumes = {"application/json", "application/json;charset=UTF-8"})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cet email est déjà utilisé.");
        }
    
        // Encode le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    
        // Active le compte
        user.setActive(true);
    
        // Assigne le rôle par défaut
        Role role = roleRepository.findById(2L)
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        user.setRole(role);
    
        User savedUser = userService.createUser(user);
    
        // Sécurité : ne pas exposer le mot de passe
        savedUser.setPassword(null);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    @GetMapping("/me/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        UserDTO userDTO = userService.mapToDTO(user);

        return ResponseEntity.ok(userDTO);
    }


    // @GetMapping("/me/{email}")
    // public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
    //     Optional<User> userOpt = userService.getUserByEmail(email);
    
    //     if (userOpt.isEmpty()) {
    //         return ResponseEntity.notFound().build();
    //     }
    
    //     User user = userOpt.get();
    //     return ResponseEntity.ok(user);
    // }
    
    
}
