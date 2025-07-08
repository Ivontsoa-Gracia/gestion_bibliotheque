package com.example.gestion_bibliotheque.service.loan.impl;

import com.example.gestion_bibliotheque.entity.book.Book;
import com.example.gestion_bibliotheque.entity.book.BookCopy;
import com.example.gestion_bibliotheque.entity.loan.Loan;
import com.example.gestion_bibliotheque.entity.loan.LoanPolicy;
import com.example.gestion_bibliotheque.entity.loan.Penalty;
import com.example.gestion_bibliotheque.entity.logs.ActivityLog;
import com.example.gestion_bibliotheque.entity.user.User;
import com.example.gestion_bibliotheque.enums.CopyStatus;
import com.example.gestion_bibliotheque.enums.LoanType;
import com.example.gestion_bibliotheque.exception.BusinessException;
import com.example.gestion_bibliotheque.repository.book.BookCopyRepository;
import com.example.gestion_bibliotheque.repository.book.BookRepository;
import com.example.gestion_bibliotheque.repository.loan.HolidayRepository;
import com.example.gestion_bibliotheque.repository.loan.LoanRepository;
import com.example.gestion_bibliotheque.repository.loan.PenaltyRepository;
import com.example.gestion_bibliotheque.repository.logs.LogRepository;
import com.example.gestion_bibliotheque.repository.user.UserRepository;
import com.example.gestion_bibliotheque.service.loan.DateUtils;
import com.example.gestion_bibliotheque.service.loan.LoanPolicyService;
import com.example.gestion_bibliotheque.service.loan.LoanService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.time.DayOfWeek;

import com.example.gestion_bibliotheque.dto.loan.LoanDTO;
import java.util.stream.Collectors;


@Service
public class LoanServiceImpl implements LoanService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LoanRepository loanRepository;
    private final LoanPolicyService loanPolicyService;
    private final HolidayRepository holidayRepository;
    private final PenaltyRepository penaltyRepository;
    private final LogRepository logRepository;

    private LoanDTO mapToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setUserId(loan.getUser().getId());
        dto.setUserName(loan.getUser().getName());
        dto.setBookCopyId(loan.getBookCopy().getId());
    
        if (loan.getBookCopy().getBook() != null) {
            dto.setBookTitle(loan.getBookCopy().getBook().getTitle());
            dto.setBookAuthor(loan.getBookCopy().getBook().getAuthor());
        }
    
        dto.setStartDate(loan.getStartDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setExtended(Boolean.TRUE.equals(loan.getExtended()));
        dto.setReturned(loan.isReturned());
        dto.setLoanType(loan.getLoanType());
    
        return dto;
    }
    

    public LoanServiceImpl(
            UserRepository userRepository,
            BookRepository bookRepository,
            BookCopyRepository bookCopyRepository,
            LoanRepository loanRepository,
            LoanPolicyService loanPolicyService,
            HolidayRepository holidayRepository,
            PenaltyRepository penaltyRepository,
            LogRepository logRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.loanRepository = loanRepository;
        this.loanPolicyService = loanPolicyService;
        this.holidayRepository = holidayRepository;
        this.penaltyRepository = penaltyRepository;
        this.logRepository = logRepository;
    }

    public static boolean isWeekendOrHoliday(LocalDate date ,HolidayRepository holidayRepository) {
        DayOfWeek day = date.getDayOfWeek();
        // boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        boolean isWeekend = (day == DayOfWeek.SUNDAY);
        boolean isHoliday = holidayRepository.existsByDate(date);
        return isWeekend || isHoliday;
    }

    private Loan validateLoanRules(Long userId, Long bookId, LoanType loanType, LocalDate startDate) throws BusinessException {
        // 1. Récupérer l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Emprunt impossible : utilisateur introuvable."));
    
        // 🔒 Vérifier si l'utilisateur est actif
        if (!user.isActive()) {
            throw new BusinessException("Emprunt refusé : l'utilisateur est inactif (abonnement expiré ou désactivé).");
        }
    
        // 2. Récupérer le livre
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("Emprunt impossible : livre introuvable."));
    
        // 3. Chercher un exemplaire disponible
        Optional<Object> availableCopyOpt = bookCopyRepository.findByBookAndStatus(book, CopyStatus.DISPONIBLE)
                .stream()
                .findFirst();
    
        if (availableCopyOpt.isEmpty()) {
            throw new BusinessException("Emprunt refusé : aucun exemplaire disponible pour ce livre.");
        }
    
        BookCopy availableCopy = (BookCopy) availableCopyOpt.get();
    
        // 4. Politique de prêt
        LoanPolicy policy = loanPolicyService.findByUserRoleAndLoanType(user.getProfile(), loanType)
                .orElseThrow(() -> new BusinessException("Emprunt impossible : politique de prêt introuvable pour ce profil."));
    
        // 5. Vérifier nombre de prêts en cours
        int currentLoans = loanRepository.countByUserAndReturnedFalse(user);
        if (currentLoans >= policy.getMaxLoans()) {
            throw new BusinessException("Emprunt refusé : nombre maximal de prêts atteint pour ce profil.");
        }
    
        // 6. Calcul de la date de retour (échéance)
        LocalDate dueDate = DateUtils.calculateDueDate(startDate, policy);
        while (isWeekendOrHoliday(dueDate, holidayRepository)) {
            dueDate = dueDate.plusDays(1);
        }
    
        // 7. Créer le prêt
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBookCopy(availableCopy);
        loan.setStartDate(startDate);
        loan.setDueDate(dueDate);
        loan.setExtended(false);
        loan.setReturnDate(null);
        loan.setLoanType(loanType);
    
        // 8. Marquer l'exemplaire comme emprunté
        availableCopy.setStatus(CopyStatus.EMPRUNTE);
        bookCopyRepository.save(availableCopy);
    
        // 9. Logger
        ActivityLog log = new ActivityLog(
                user,
                "EMPRUNT",
                "Emprunt du livre : " + availableCopy.getCode(),
                dueDate.atStartOfDay());
        logRepository.save(log);
    
        return loan;
    }
    

    @Override
    public Loan borrowBook(Long userId, Long bookId, LoanType loanType, LocalDate start_date) throws BusinessException {
//        validation de pret
        Loan loan = validateLoanRules(userId, bookId, loanType, start_date);


        // 7. Enregistrer et retourner le prêt
        return loanRepository.save(loan);
    }

    private void checkingPenalty(LocalDate returnDate, Loan loan){
    ActivityLog log ;
        // Vérifier le retard
        if (returnDate.isAfter(loan.getDueDate())) {
            int daysLate = (int) ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);

            Penalty penalty = new Penalty();
            penalty.setUser(loan.getUser());
            penalty.setStartDate(returnDate);
            penalty.setDays(daysLate);
            penalty.setEndDate(returnDate.plusDays(daysLate));
            penalty.setReason("Retour en retard de " + daysLate + " jour(s)");
            penalty.setActive(true);

            penaltyRepository.save(penalty);

             log = new ActivityLog(
                    loan.getUser(),
                    "PENALITE",
                    "Pénalité appliquée pour retour tardif du livre : " + loan.getBookCopy().getCode() +
                            " (" + daysLate + " jour(s) de retard)",
                    returnDate.atStartOfDay());

        }else {
             log = new ActivityLog(
                    loan.getUser(),
                    "RETOUR",
                    "Retour du livre : " + loan.getBookCopy().getCode(),
                    returnDate.atStartOfDay());
        }

        logRepository.save(log);
    }

    public Loan returnBook(Long loanId, LocalDate returnDate) throws BusinessException {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Prêt introuvable"));

        if (loan.isReturned()) {
            throw new BusinessException("Le livre a déjà été retourné");
        }

        if (returnDate.isBefore(loan.getStartDate())) {
            throw new BusinessException("La date de retour ne peut pas être antérieure à la date de prêt");
        }

         checkingPenalty(returnDate, loan);

        // Mettre à jour le prêt
        loan.setReturned(true);
        loan.setReturnDate(returnDate);

        // Mettre l'exemplaire en DISPONIBLE
        BookCopy copy = loan.getBookCopy();
        copy.setStatus(CopyStatus.DISPONIBLE);
        bookCopyRepository.save(copy);

        // Logger l’action
        ActivityLog log = new ActivityLog(
                loan.getUser(),
                "RETOUR",
                "Retour du livre : " + copy.getCode(),
                returnDate.atStartOfDay());

        logRepository.save(log);

        return loanRepository.save(loan);
    }


    @Override
    public List<Loan> getLoans(LocalDate start_date) {
        return loanRepository.findAllByStartDate(start_date);
    }

    @Override
    public List<Loan> getLoans(LocalDate start_date, LocalDate end_date) {
        return loanRepository.findAllByStartDateAndAndDueDate(start_date,end_date);
    }

    @Override
    public List<Loan> getLoans(LocalDate start_date, LocalDate end_date, LoanType loanType) {
        return loanRepository.findAllByStartDateAndDueDateAndLoanType(start_date, end_date, loanType);

    }

    // @Override
    // public List<Loan> getLoans() {
    //     return loanRepository.findAll();
    // }

    @Override
    public List<LoanDTO> getLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public List<LoanDTO> getLoansByUserId(Long userId) {
        List<Loan> loans = loanRepository.findByUserId(userId);
        return loans.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private LoanDTO convertToDto(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setUserId(loan.getUser().getId());
        dto.setUserName(loan.getUser().getName());
        dto.setBookCopyId(loan.getBookCopy().getId());
        dto.setBookTitle(loan.getBookCopy().getBook().getTitle());
        dto.setBookAuthor(loan.getBookCopy().getBook().getAuthor());
        dto.setStartDate(loan.getStartDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setExtended(loan.getExtended()); 
        dto.setLoanType(loan.getLoanType());
        return dto;
    }


}