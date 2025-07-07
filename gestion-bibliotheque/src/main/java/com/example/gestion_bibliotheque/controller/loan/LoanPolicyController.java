package com.example.gestion_bibliotheque.controller.loan;

import com.example.gestion_bibliotheque.entity.loan.LoanPolicy;
import com.example.gestion_bibliotheque.service.loan.LoanPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class LoanPolicyController {

    private final LoanPolicyService loanPolicyService;

    public LoanPolicyController(LoanPolicyService loanPolicyService) {
        this.loanPolicyService = loanPolicyService;
    }

    @GetMapping
    public ResponseEntity<List<LoanPolicy>> getAll() {
        return ResponseEntity.ok(loanPolicyService.getAllPolicies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanPolicy> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanPolicyService.getPolicyById(id));
    }

    @PostMapping
    public ResponseEntity<LoanPolicy> create(@RequestBody LoanPolicy policy) {
        return ResponseEntity.ok(loanPolicyService.createPolicy(policy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanPolicy> update(@PathVariable Long id, @RequestBody LoanPolicy policy) {
        return ResponseEntity.ok(loanPolicyService.updatePolicy(id, policy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
