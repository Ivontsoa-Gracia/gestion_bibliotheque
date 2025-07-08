package com.example.gestion_bibliotheque.entity.user;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "abonnements")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
