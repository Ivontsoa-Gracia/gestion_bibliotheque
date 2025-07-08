package com.example.gestion_bibliotheque.controller.loan;

import com.example.gestion_bibliotheque.dto.loan.ReservationDTO;
import com.example.gestion_bibliotheque.entity.loan.Reservation;
import com.example.gestion_bibliotheque.exception.BusinessException;
import com.example.gestion_bibliotheque.service.loan.impl.ReservationServiceImpl;
import com.example.gestion_bibliotheque.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationServiceImpl reservationService;
    private final UserService userService;

    public ReservationController(ReservationServiceImpl reservationService, UserService userService, UserService userService1) {
        this.reservationService = reservationService;
        this.userService = userService1;
    }

    // Voir tous les réservations (admin)
    // @GetMapping
    // public ResponseEntity<List<ReservationDTO>> getAll() {
    //     List<ReservationDTO> dtoList = reservationService.findAll().stream()
    //             .map(ReservationDTO::fromEntity)
    //             .collect(Collectors.toList());
    //     return ResponseEntity.ok(dtoList);
    // }


    @GetMapping("/me")
    public ResponseEntity<List<ReservationDTO>> getMyReservations(@RequestParam Long userId) throws BusinessException {
        List<Reservation> reservations = reservationService.findByUser(userId);
        List<ReservationDTO> dtoList = reservations.stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAll() {
        List<ReservationDTO> dtoList = reservationService.findAll().stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getById(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(reservation -> ResponseEntity.ok(ReservationDTO.fromEntity(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/reserve")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO request) {
        try {
            validateReservationRequest(request);
            Reservation reservation = reservationService.create(
                   request
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);

        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations/user/{userId}")
    public ResponseEntity<?> getUserReservations(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getActiveReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    // Validation de la requête de réservation (séparation des règles)
    private void validateReservationRequest(ReservationDTO request) throws BusinessException {
        checkUserExists(request.getUserId());
        checkBookExists(request.getBookId());
        checkBookAvailability(request.getBookId());
        checkUserHasNoActiveReservationForBook(request.getUserId(), request.getBookId());
        checkUserReservationLimit(request.getUserId());
    }

    private void checkUserExists(Long userId) throws BusinessException {
        if (!userService.existsById(userId)) {
            throw new BusinessException("Utilisateur non trouvé.");
        }
    }

    private void checkBookExists(Long bookId) throws BusinessException {
        if (!reservationService.bookExists(bookId)) {
            throw new BusinessException("Livre non trouvé.");
        }
    }

    private void checkBookAvailability(Long bookId) throws BusinessException {
        boolean available = reservationService.isBookAvailable(bookId);
        if (available) {
            throw new BusinessException("Le livre est disponible, réservation non possible.");
        }
    }

    private void checkUserHasNoActiveReservationForBook(Long userId, Long bookId) throws BusinessException {
        boolean hasReservation = reservationService.userHasActiveReservationForBook(userId, bookId);
        if (hasReservation) {
            throw new BusinessException("Vous avez déjà une réservation active pour ce livre.");
        }
    }

    private void checkUserReservationLimit(Long userId) throws BusinessException {
        int maxReservations = 5; 
        int currentReservations = reservationService.countActiveReservationsByUser(userId);
        if (currentReservations >= maxReservations) {
            throw new BusinessException("Limite de réservations actives atteinte.");
        }
    }
}