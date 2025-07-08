package com.example.gestion_bibliotheque.service.loan.impl;

import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.entity.loan.Prolongement;
import com.example.gestion_bibliotheque.exception.BusinessException;
import com.example.gestion_bibliotheque.repository.loan.LoanRepository;
import com.example.gestion_bibliotheque.repository.loan.ProlongementRepository;
import com.example.gestion_bibliotheque.service.loan.ProlongementService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProlongementServiceImpl implements ProlongementService {

    private final ProlongementRepository prolongementRepository;
    private final LoanRepository loanRepository;

    public ProlongementServiceImpl(ProlongementRepository prolongementRepository, LoanRepository loanRepository) {
        this.prolongementRepository = prolongementRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public Prolongement demanderProlongement(Long loanId, int duree) throws BusinessException {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Prêt introuvable"));

        Prolongement prolongement = new Prolongement();
        prolongement.setLoan(loan);
        prolongement.setDuree(duree);
        prolongement.setStatus(false);
        prolongement.setDemandeDate(LocalDate.now());

        return prolongementRepository.save(prolongement);
    }

    @Override
    public List<Prolongement> getAllDemandes() {
        return prolongementRepository.findAll();
    }
    
    @Override
    public Prolongement validerProlongement(Long prolongementId) throws BusinessException {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new BusinessException("Prolongement introuvable"));
    
        Loan loan = prolongement.getLoan();
        User user = loan.getUser();
    
        LocalDate prolongationStart = loan.getDueDate().plusDays(1);
        LocalDate prolongationEnd = loan.getDueDate().plusDays(prolongement.getDuree());
    
        // Vérifier que l'utilisateur a un abonnement actif couvrant toute la période
        if (!user.hasActiveAbonnementBetween(prolongationStart, prolongationEnd)) {
            throw new BusinessException("Prolongement refusé : abonnement inactif pendant toute la période du prolongement.");
        }
    
        loan.setDueDate(prolongationEnd);
        loan.setExtended(true);
    
        prolongement.setStatus(true);
    
        loanRepository.save(loan);
        return prolongementRepository.save(prolongement);
    }
    

    // @Override
    // public Prolongement validerProlongement(Long prolongementId) throws BusinessException {
    //     Prolongement prolongement = prolongementRepository.findById(prolongementId)
    //             .orElseThrow(() -> new BusinessException("Prolongement introuvable"));

    //     Loan loan = prolongement.getLoan();
    //     loan.setDueDate(loan.getDueDate().plusDays(prolongement.getDuree()));
    //     loan.setExtended(true);

    //     prolongement.setStatus(true);

    //     loanRepository.save(loan);
    //     return prolongementRepository.save(prolongement);
    // }
}
