package com.example.gestion_bibliotheque.dto.user;

import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.entity.loan.Penalty;
import com.example.gestion_bibliotheque.entity.loan.Reservation;
import com.example.gestion_bibliotheque.enums.UserProfil;
import com.example.gestion_bibliotheque.entity.user.Role;
import com.example.gestion_bibliotheque.dto.loan.LoanDTO;

import java.util.List;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private UserProfil profile;
    private boolean active;
    private Role role;
    private String roleName;

    private List<LoanDTO> loans;
    private List<Reservation> reservations;
    private List<Penalty> penalties;

    public UserDTO() {}

    public UserDTO(Long id, String name, String email, UserProfil profile, boolean active,
        Role role, List<LoanDTO> loans, List<Reservation> reservations, List<Penalty> penalties) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.profile = profile;
    this.active = active;
    this.role = role;
    this.loans = loans;  
    this.reservations = reservations;
    this.penalties = penalties;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserProfil getProfile() { return profile; }
    public void setProfile(UserProfil profile) { this.profile = profile; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<LoanDTO> getLoans() { return loans; }
    public void setLoans(List<LoanDTO> loans) { this.loans = loans; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }

    public List<Penalty> getPenalties() { return penalties; }
    public void setPenalties(List<Penalty> penalties) { this.penalties = penalties; }
}
