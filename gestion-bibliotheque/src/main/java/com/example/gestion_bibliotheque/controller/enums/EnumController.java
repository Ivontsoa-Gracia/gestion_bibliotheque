package com.example.gestion_bibliotheque.controller.enums;

import com.example.gestion_bibliotheque.enums.UserProfil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gestion_bibliotheque.enums.LoanType;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping("/user-profils")
    public ResponseEntity<UserProfil[]> getUserProfils() {
        return ResponseEntity.ok(UserProfil.values());
    }

    @GetMapping("/loan-types")
    public ResponseEntity<LoanType[]> getLoanTypes() {
        return ResponseEntity.ok(LoanType.values());
    }
}
