package rs.fon.bg.ac.rs.lukic_matija.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("api/tournaments/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody TournamentAddDto tUpdate){
        try {
            TournamentResponseDto tR = tournamentService.updateTournament(id, tUpdate);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(tR);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Update tournament unsuccessful");
        }
    }

    @DeleteMapping("api/tournaments/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try {
            tournamentService.deleteTournament(id);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("Deleted");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Delete tournament unsuccessful");
        }
    }
}
