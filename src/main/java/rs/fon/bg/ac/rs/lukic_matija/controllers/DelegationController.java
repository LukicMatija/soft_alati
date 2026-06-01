package rs.fon.bg.ac.rs.lukic_matija.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos.DelegationAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos.DelegationResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.services.DelegationService;

@RestController
public class DelegationController {
    private final DelegationService delegationService;

    public DelegationController(DelegationService delegationService) {
        this.delegationService = delegationService;
    }

    @PostMapping("api/delegations")
    public ResponseEntity<Object> create(@RequestBody DelegationAddDto dAdd){
        try {
            DelegationResponseDto dR = delegationService.create(dAdd);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dR);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Create delegation unsuccessful");
        }
    }

    @PutMapping("api/delegations/{id}")
    public ResponseEntity<Object> updateScore(@PathVariable Long id,@RequestParam double score){
        try {
            DelegationResponseDto dR = delegationService.updateScore(id, score);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dR);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Update delegation unsuccessful");
        }
    }
}
