package com.example.gestion_bibliotheque.entity.user;

import jakarta.persistence.*;
import java.util.List;
import com.example.gestion_bibliotheque.enums.UserProfil;
import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.entity.loan.Reservation;
import com.example.gestion_bibliotheque.entity.loan.Penalty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserProfil profile;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private List<Penalty> penalties;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Abonnement> abonnements;

    public User() {
    }

    public User(Long id, String name, String email, String password, UserProfil profile, boolean active, Role role, List<Loan> loans, List<Reservation> reservations, List<Penalty> penalties) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.active = active;
        this.role = role;
        this.loans = loans;
        this.reservations = reservations;
        this.penalties = penalties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Penalty> getPenalties() {
        return penalties;
    }

    public void setPenalties(List<Penalty> penalties) {
        this.penalties = penalties;
    }

    public UserProfil getProfile() {
        return profile;
    }

    public void setProfile(UserProfil profile) {
        this.profile = profile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

     /**
     * Vérifie si l'utilisateur a au moins un abonnement actif
     * couvrant toute la période [start, end].
     */
    public boolean hasActiveAbonnementBetween(LocalDate start, LocalDate end) {
        if (abonnements == null || abonnements.isEmpty()) {
            return false;
        }
        return abonnements.stream()
                .anyMatch(abonnement ->
                    abonnement.isStatus() && // actif
                    (abonnement.getDateDebut() == null || !abonnement.getDateDebut().isAfter(start)) &&
                    (abonnement.getDateFin() == null || !abonnement.getDateFin().isBefore(end))
                );
    }
}
