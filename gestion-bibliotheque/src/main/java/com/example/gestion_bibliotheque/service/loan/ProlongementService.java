package com.example.gestion_bibliotheque.service.loan;

import com.example.gestion_bibliotheque.entity.loan.Prolongement;
import com.example.gestion_bibliotheque.exception.BusinessException;

import java.util.List;

public interface ProlongementService {

    Prolongement demanderProlongement(Long loanId, int duree) throws BusinessException;

    List<Prolongement> getAllDemandes();

    Prolongement validerProlongement(Long prolongementId) throws BusinessException;

}
