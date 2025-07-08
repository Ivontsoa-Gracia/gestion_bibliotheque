package com.example.gestion_bibliotheque.controller;

import com.example.gestion_bibliotheque.entity.loan.Prolongement;
import com.example.gestion_bibliotheque.service.loan.ProlongementService;
import com.example.gestion_bibliotheque.exception.BusinessException;

import org.springframework.web.bind.annotation.*;
import com.example.gestion_bibliotheque.dto.loan.ProlongementDTO;
import com.example.gestion_bibliotheque.dto.loan.ExtendDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prolongements")
public class ProlongementController {

    private final ProlongementService prolongementService;

    public ProlongementController(ProlongementService prolongementService) {
        this.prolongementService = prolongementService;
    }

    @PostMapping("/demande")
    public Prolongement demanderProlongement(@RequestBody ProlongementDTO dto) {
        return prolongementService.demanderProlongement(dto.getLoanId(), dto.getDuree());
    }
    

    @GetMapping("/all")
    public List<ExtendDTO> getAll() {
        return prolongementService.getAllDemandes()
            .stream()
            .map(ExtendDTO::new)
            .collect(Collectors.toList());
    }
    

    @PutMapping("/valider/{id}")
    public Prolongement valider(@PathVariable Long id) throws BusinessException{
        return prolongementService.validerProlongement(id);
    }
}
