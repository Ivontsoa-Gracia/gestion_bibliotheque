package com.example.gestion_bibliotheque.service.user.impl;

import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.repository.user.UserRepository;
import com.example.gestion_bibliotheque.service.user.UserService;
import org.springframework.stereotype.Service;
import com.example.gestion_bibliotheque.enums.UserProfil;
import com.example.gestion_bibliotheque.dto.loan.LoanDTO;
import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.entity.book.Book;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import com.example.gestion_bibliotheque.dto.user.UserDTO;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        user.setActive(true); 
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private List<LoanDTO> mapLoans(List<Loan> loans) {
        return loans.stream().map(loan -> {
            LoanDTO dto = new LoanDTO();
            dto.setId(loan.getId());
            dto.setUserId(loan.getUser().getId());
            dto.setUserName(loan.getUser().getName());
            dto.setBookCopyId(loan.getBookCopy().getId());
            dto.setBookTitle(loan.getBookCopy().getBook().getTitle());
            dto.setBookAuthor(loan.getBookCopy().getBook().getAuthor());
            dto.setStartDate(loan.getStartDate());
            dto.setDueDate(loan.getDueDate());
            dto.setReturnDate(loan.getReturnDate());
            dto.setExtended(loan.getExtended()); 
            dto.setReturned(loan.isReturned());
            dto.setLoanType(loan.getLoanType());
            return dto;
        }).collect(Collectors.toList());
    }
    
    public UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfile(user.getProfile());
        dto.setActive(user.isActive());
        dto.setRole(user.getRole());
        dto.setRoleName(user.getRole().getName());
        return dto;
    }

    // public UserDTO mapToDTO(User user) {
    //     UserDTO dto = new UserDTO();
    //     dto.setId(user.getId());
    //     dto.setName(user.getName());
    //     dto.setEmail(user.getEmail());
    //     dto.setProfile(user.getProfile());
    //     dto.setActive(user.isActive());
    //     dto.setRole(user.getRole());
    //     dto.setRoleName(user.getRole().getName());
    //     dto.setLoans(mapLoans(user.getLoans()));  
    //     dto.setReservations(user.getReservations());
    //     dto.setPenalties(user.getPenalties());
    //     return dto;
    // }
    
    

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setRole(updatedUser.getRole());
                    user.setProfile(updatedUser.getProfile());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void activateUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(true);
            userRepository.save(user);
        });
    }

    @Override
    public void deactivateUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
        });
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
    
            if (user.getPassword().equals(password) && user.getProfile() == UserProfil.BIBLIOTHECAIRE && user.isActive()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        return userRepository.findByRoleId(roleId);
    }

    @Override
    public UserDTO getDataUserById(Long id) {
        User user = userRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("User not found"));
    
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfile(user.getProfile());
        dto.setActive(user.isActive());
        dto.setRoleName(user.getRole().getName());
        dto.setLoans(mapLoans(user.getLoans()));
    
        return dto;
    }
    


}
