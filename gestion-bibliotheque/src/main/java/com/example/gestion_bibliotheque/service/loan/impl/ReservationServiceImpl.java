package com.example.gestion_bibliotheque.service.loan.impl;

import com.example.gestion_bibliotheque.entity.book.Book;
import com.example.gestion_bibliotheque.entity.loan.Reservation;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.enums.CopyStatus;
import com.example.gestion_bibliotheque.enums.ReservationStatus;
import com.example.gestion_bibliotheque.repository.book.BookCopyRepository;
import com.example.gestion_bibliotheque.repository.book.BookRepository;
import com.example.gestion_bibliotheque.repository.loan.ReservationRepository;
import com.example.gestion_bibliotheque.repository.user.UserRepository;
import com.example.gestion_bibliotheque.service.loan.ReservationService;
import org.springframework.stereotype.Service;
import com.example.gestion_bibliotheque.entity.loan.LoanPolicy;
import com.example.gestion_bibliotheque.service.loan.LoanPolicyService;
import com.example.gestion_bibliotheque.enums.LoanType;
import com.example.gestion_bibliotheque.exception.BusinessException;
import com.example.gestion_bibliotheque.enums.ReservationStatus;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.gestion_bibliotheque.dto.loan.ReservationDTO;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LoanPolicyService loanPolicyService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository, BookRepository bookRepository, BookCopyRepository bookCopyRepository,  LoanPolicyService loanPolicyService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.loanPolicyService = loanPolicyService;
         
    }

    @Override
    public List<Reservation> findByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public Reservation create(ReservationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(dto.getBookId())
            .orElseThrow(() -> new RuntimeException("Book not found"));

        // Charger la politique de prêt (réservation = type DOMICILE ou autre selon toi)
        LoanPolicy policy = loanPolicyService.findByUserRoleAndLoanType(user.getProfile(), LoanType.DOMICILE)
            .orElseThrow(() -> new BusinessException("Réservation impossible : politique introuvable pour ce profil."));

        // Vérifier le nombre actuel de réservations
        int currentReservations = reservationRepository.countByUserAndStatus(user, ReservationStatus.EN_ATTENTE);
        if (currentReservations >= policy.getMaxReservation()) {
            throw new BusinessException("Réservation refusée : nombre maximal de réservations atteint pour ce profil.");
        }

        Reservation reservation = dto.toEntity(user, book);
        reservation.setStatus(ReservationStatus.EN_ATTENTE);
        reservation.setReservationDate(LocalDate.now());
        return reservationRepository.save(reservation);
    }


    // @Override
    // public Reservation create(ReservationDTO dto) {
    //     User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
    //     Book book = bookRepository.findById(dto.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));

    //     Reservation reservation = dto.toEntity(user, book);
    //     reservation.setStatus(ReservationStatus.EN_ATTENTE);
    //     reservation.setReservationDate(LocalDate.now());
    //     return reservationRepository.save(reservation);
    // }

    @Override
    public void cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setActive(false);
        reservation.setStatus(ReservationStatus.ANNULEE);
        reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getActiveReservationsByUser(Long userId) {
        return reservationRepository.findByUserIdAndActiveTrue(userId);
    }

    @Override
    public boolean bookExists(Long bookId) {
        return bookRepository.existsById(bookId);
    }

    @Override
    public boolean isBookAvailable(Long bookId) {
        long availableCopiesCount = bookCopyRepository.countByBookIdAndStatus(bookId, CopyStatus.DISPONIBLE);
        return availableCopiesCount > 0;
    }
    @Override
    public boolean userHasActiveReservationForBook(Long userId, Long bookId) {
        return reservationRepository.existsByUserIdAndBookIdAndActiveTrue(userId, bookId);
    }
    @Override
    public int countActiveReservationsByUser(Long userId) {
        return (int) reservationRepository.countByUserIdAndActiveTrue(userId);
    }
}