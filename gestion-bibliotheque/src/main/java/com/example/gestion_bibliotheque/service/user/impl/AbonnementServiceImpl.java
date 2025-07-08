package com.example.gestion_bibliotheque.service.user.impl;

import com.example.gestion_bibliotheque.entity.user.Abonnement;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.repository.user.AbonnementRepository;
import com.example.gestion_bibliotheque.repository.user.UserRepository;
import com.example.gestion_bibliotheque.service.user.AbonnementService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AbonnementServiceImpl implements AbonnementService {

    private final AbonnementRepository abonnementRepository;
    private final UserRepository userRepository;

    public AbonnementServiceImpl(AbonnementRepository abonnementRepository, UserRepository userRepository) {
        this.abonnementRepository = abonnementRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Abonnement ajouterAbonnement(Abonnement abonnement) {
        User user = abonnement.getUser();
    
        // Désactiver le dernier abonnement expiré
        Optional<Abonnement> last = abonnementRepository.findLastAbonnementByUser(user);
        if (last.isPresent()) {
            Abonnement dernier = last.get();
            if (dernier.getDateFin().isBefore(LocalDate.now())) {
                dernier.setStatus(false);
                abonnementRepository.save(dernier);
            }
        }
    
        // Gérer le statut actif de l'utilisateur en fonction du nouvel abonnement
        if (abonnement.isStatus()) {
            user.setActive(true);
        } else {
            user.setActive(false);
        }
        userRepository.save(user);
    
        return abonnementRepository.save(abonnement);
    }
    
}
