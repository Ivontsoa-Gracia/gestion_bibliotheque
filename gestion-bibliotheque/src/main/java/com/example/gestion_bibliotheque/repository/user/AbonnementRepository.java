package com.example.gestion_bibliotheque.repository.user;

import com.example.gestion_bibliotheque.entity.user.Abonnement;
import com.example.gestion_bibliotheque.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    @Query("SELECT a FROM Abonnement a WHERE a.user = :user ORDER BY a.dateFin DESC LIMIT 1")
    Optional<Abonnement> findLastAbonnementByUser(User user);
}
