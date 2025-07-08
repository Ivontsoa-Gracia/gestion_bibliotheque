package com.example.gestion_bibliotheque.controller.user;

import com.example.gestion_bibliotheque.entity.user.Abonnement;
import com.example.gestion_bibliotheque.service.user.AbonnementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gestion_bibliotheque.dto.user.AbonnementDTO;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.repository.user.*;
import java.util.Optional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/abonnements")
public class AbonnementController {

    private final AbonnementService abonnementService;
    private final UserRepository userRepository;
    private final AbonnementRepository abonnementRepository;

    public AbonnementController(AbonnementService abonnementService, UserRepository userRepository, AbonnementRepository abonnementRepository) {
        this.abonnementService = abonnementService;
        this.userRepository = userRepository;
        this.abonnementRepository = abonnementRepository;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<String> ajouterAbonnement(@RequestBody AbonnementDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé avec l'ID : " + dto.getUserId());
        }

        Abonnement abonnement = new Abonnement();
        abonnement.setUser(userOpt.get());
        abonnement.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        abonnement.setDateFin(LocalDate.parse(dto.getDateFin()));
        abonnement.setStatus(dto.isStatus());

        abonnementRepository.save(abonnement);
        return ResponseEntity.ok("✅ Abonnement ajouté avec succès !");
    }
}