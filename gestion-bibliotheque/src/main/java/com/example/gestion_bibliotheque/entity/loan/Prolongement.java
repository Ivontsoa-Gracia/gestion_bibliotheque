package com.example.gestion_bibliotheque.entity.loan;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Prolongement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private boolean status; 

    private int duree; 

    private LocalDate demandeDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan = loan; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public LocalDate getDemandeDate() { return demandeDate; }
    public void setDemandeDate(LocalDate demandeDate) { this.demandeDate = demandeDate; }
}
