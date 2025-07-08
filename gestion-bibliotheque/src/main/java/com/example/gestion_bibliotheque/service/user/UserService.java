package com.example.gestion_bibliotheque.service.user;

import com.example.gestion_bibliotheque.entity.user.User;
import java.util.List;
import java.util.Optional;
import com.example.gestion_bibliotheque.dto.user.UserDTO;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    void activateUser(Long id);

    void deactivateUser(Long id);

    Optional<User> authenticate(String email, String password);

    Optional<User> getUserByEmail(String email);

    boolean existsById(Long userId);

    List<User> getUsersByRoleId(Long roleId);

    UserDTO getDataUserById(Long id);

    UserDTO mapToDTO(User user);

}
