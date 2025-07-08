package com.example.gestion_bibliotheque.repository.loan;

import com.example.gestion_bibliotheque.entity.loan.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.enums.ReservationStatus;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
   
    List<Reservation> findByUserIdAndActiveTrue(Long userId);

    List<Reservation> findByBookId(Long bookId);

    long countByUserIdAndActiveTrue(Long userId);

    List<Reservation> findByUserId(Long userId);

    boolean existsByUserIdAndBookIdAndActiveTrue(Long userId, Long bookId);

    int countByUserAndStatus(User user, ReservationStatus status);

}