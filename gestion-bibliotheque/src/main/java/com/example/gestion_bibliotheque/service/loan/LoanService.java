package com.example.gestion_bibliotheque.service.loan;


import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.enums.LoanType;
import com.example.gestion_bibliotheque.exception.BusinessException;
import java.util.List;

import java.time.LocalDate;
import com.example.gestion_bibliotheque.dto.loan.LoanDTO;


public interface  LoanService {

    Loan borrowBook(Long userId, Long bookId, LoanType loanType, LocalDate start_date) throws BusinessException;

    Loan returnBook(Long loanId,LocalDate returnDate) throws BusinessException;

    List<Loan> getLoans(LocalDate start_date);
    List<Loan> getLoans(LocalDate start_date, LocalDate end_date);
    List<Loan> getLoans(LocalDate start_date, LocalDate end_date, LoanType loanType);
    // List<Loan> getLoans();

    List<LoanDTO> getLoans();

    List<LoanDTO> getLoansByUserId(Long userId); 


}