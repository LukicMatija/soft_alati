package rs.fon.bg.ac.rs.lukic_matija.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos.SponsorAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos.SponsorResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.services.SponsorService;

@RestController
public class SponsorController {
    private final SponsorService sponsorService;

    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @PostMapping("api/sponsors")
    public ResponseEntity<Object> create(@RequestBody SponsorAddDto sAdd){
        try {
            SponsorResponseDto sR = sponsorService.create(sAdd);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(sR);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Create sponsor unsuccessful");
        }
    }
}
