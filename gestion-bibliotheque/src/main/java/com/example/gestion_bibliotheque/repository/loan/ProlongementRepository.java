package com.example.gestion_bibliotheque.repository.loan;

import com.example.gestion_bibliotheque.entity.loan.Prolongement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {
}
