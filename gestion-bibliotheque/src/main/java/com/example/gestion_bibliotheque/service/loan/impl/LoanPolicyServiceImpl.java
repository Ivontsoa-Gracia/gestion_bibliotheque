package com.example.gestion_bibliotheque.service.loan.impl;

import com.example.gestion_bibliotheque.entity.loan.LoanPolicy;
import com.example.gestion_bibliotheque.enums.LoanType;
import com.example.gestion_bibliotheque.enums.UserProfil;
import com.example.gestion_bibliotheque.repository.loan.LoanPolicyRepository;
import com.example.gestion_bibliotheque.service.loan.LoanPolicyService;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;


@Service
public class LoanPolicyServiceImpl implements LoanPolicyService {

    private final LoanPolicyRepository policyRepository;

    public LoanPolicyServiceImpl(LoanPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public List<LoanPolicy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public LoanPolicy getPolicyById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Politique non trouvée avec l'id: " + id));
    }

    @Override
    public LoanPolicy createPolicy(LoanPolicy policy) {
        return policyRepository.save(policy);
    }

    @Override
    public LoanPolicy updatePolicy(Long id, LoanPolicy updatedPolicy) {
        LoanPolicy existing = getPolicyById(id);
        existing.setUserProfil(updatedPolicy.getUserProfil());
        existing.setLoanType(updatedPolicy.getLoanType());
        existing.setMaxLoans(updatedPolicy.getMaxLoans());
        existing.setLoanDurationDays(updatedPolicy.getLoanDurationDays());
        existing.setMaxProlongations(updatedPolicy.getMaxProlongations());
        existing.setAllowProlongation(updatedPolicy.isAllowProlongation());
        existing.setAllowReservation(updatedPolicy.isAllowReservation());
        existing.setPenaltyDaysPerLateDay(updatedPolicy.getPenaltyDaysPerLateDay());

        return policyRepository.save(existing);
    }

    @Override
    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }

    @Override
    public Optional<LoanPolicy> findByUserRoleAndLoanType(UserProfil userProfil, LoanType loanType) {
        return policyRepository.findByUserProfilAndLoanType(userProfil, loanType);
    }
}