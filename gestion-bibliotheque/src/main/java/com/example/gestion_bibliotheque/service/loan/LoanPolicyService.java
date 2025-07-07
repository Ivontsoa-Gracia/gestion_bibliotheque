package com.example.gestion_bibliotheque.service.loan;

import com.example.gestion_bibliotheque.entity.loan.LoanPolicy;
import com.example.gestion_bibliotheque.enums.LoanType;
import com.example.gestion_bibliotheque.enums.UserProfil;

import java.util.Optional;
import java.util.List;


public interface LoanPolicyService {

    Optional<LoanPolicy> findByUserRoleAndLoanType(UserProfil userProfil, LoanType loanType);

    List<LoanPolicy> getAllPolicies();
    LoanPolicy getPolicyById(Long id);
    LoanPolicy createPolicy(LoanPolicy policy);
    LoanPolicy updatePolicy(Long id, LoanPolicy updatedPolicy);
    void deletePolicy(Long id);
}