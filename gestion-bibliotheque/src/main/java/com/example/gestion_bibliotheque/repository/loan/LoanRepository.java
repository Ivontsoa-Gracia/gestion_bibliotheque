package com.example.gestion_bibliotheque.repository.loan;

import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.enums.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
//    List<Loan> findByUserAndReturnedFalseAndReturnDateIsNull(User user);

    Optional<Loan> findByBookCopyIdAndReturnedFalse(Long bookCopyId);

    List<Loan> findByUserId(Long userId);

    int countByUserAndReturnedFalse(User user);

    List<Loan> findAllByStartDate(LocalDate startDate);

    List<Loan> findAllByStartDateAndAndDueDate(LocalDate start_date, LocalDate end_date);

    List<Loan> findAllByStartDateAndDueDateAndLoanType(LocalDate startDate, LocalDate dueDate, LoanType loanType);

//    List<Loan> findLoansByUserAndReturnDateIsEmptyAndReturnDateIsFalse(User user);


}