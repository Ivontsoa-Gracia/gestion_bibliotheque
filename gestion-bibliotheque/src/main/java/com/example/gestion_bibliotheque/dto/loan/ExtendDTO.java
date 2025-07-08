package com.example.gestion_bibliotheque.dto.loan;
import com.example.gestion_bibliotheque.entity.loan.Prolongement;

import java.time.LocalDate;

public class ExtendDTO {

    private Long id;            
    private Long loanId;          
    private String userName;      
    private String bookTitle;      
    private int duree;             
    private LocalDate demandeDate; 
    private boolean status;        

    public ExtendDTO() {}

    public ExtendDTO(Long id, Long loanId, String userName, String bookTitle, int duree, LocalDate demandeDate, boolean status) {
        this.id = id;
        this.loanId = loanId;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.duree = duree;
        this.demandeDate = demandeDate;
        this.status = status;
    }

    public ExtendDTO(Prolongement prolongement) {
        this.id = prolongement.getId();
        this.loanId = prolongement.getLoan().getId();
        this.userName = prolongement.getLoan().getUser().getName();
        this.bookTitle = prolongement.getLoan().getBookCopy().getBook().getTitle();
        this.duree = prolongement.getDuree();
        this.demandeDate = prolongement.getDemandeDate();
        this.status = prolongement.isStatus();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public LocalDate getDemandeDate() { return demandeDate; }
    public void setDemandeDate(LocalDate demandeDate) { this.demandeDate = demandeDate; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
