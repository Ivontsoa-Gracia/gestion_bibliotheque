package com.example.gestion_bibliotheque.dto.loan;

public class ProlongementDTO {
    private Long loanId;
    private int duree;

    public ProlongementDTO() {}

    public ProlongementDTO(Long loanId, int duree) {
        this.loanId = loanId;
        this.duree = duree;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}
