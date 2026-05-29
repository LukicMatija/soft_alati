package rs.fon.bg.ac.rs.lukic_matija.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.services.TournamentService;

@RestController
public class TournamentController {
    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService){
        this.tournamentService = tournamentService;
    }

    @PostMapping("/api/tournaments")
    public ResponseEntity<Object> post(@RequestBody TournamentAddDto tAdd){
        try {
            System.out.println(tAdd);
            TournamentResponseDto tR = tournamentService.createTournament(tAdd);
            System.out.println(tR);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(tR);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Create tournament unsuccessful");
        }
    }
}
