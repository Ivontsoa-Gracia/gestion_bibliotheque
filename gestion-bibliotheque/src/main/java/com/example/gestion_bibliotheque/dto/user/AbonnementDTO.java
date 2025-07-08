package com.example.gestion_bibliotheque.dto.user;

public class AbonnementDTO {
    private Long userId;
    private String dateDebut;
    private String dateFin;
    private boolean status;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
