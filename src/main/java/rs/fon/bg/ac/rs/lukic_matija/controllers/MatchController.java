package rs.fon.bg.ac.rs.lukic_matija.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchUpdateDto;
import rs.fon.bg.ac.rs.lukic_matija.services.MatchService;

import java.util.List;

@RestController
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("api/matches")
    public ResponseEntity<Object> create(@RequestBody MatchAddDto mAdd){
        try {
            MatchResponseDto mr = matchService.create(mAdd);
            return  ResponseEntity
                    .status(HttpStatus.OK)
                    .body(mr);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Create match unsuccessful");
        }
    }
    @PutMapping("api/matches/{tId}/{id}")
    public ResponseEntity<Object> updateRes(@PathVariable Long tId,@PathVariable Long id, @RequestBody MatchUpdateDto mUpdate){
        try {
            MatchResponseDto mr = matchService.update(tId, id, mUpdate);
            return  ResponseEntity
                    .status(HttpStatus.OK)
                    .body(mr);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Update match unsuccessful");
        }
    }
    @GetMapping("api/matches")
    public ResponseEntity<Object> getByPhase(@RequestParam String phase){
        try {
            List<MatchResponseDto> mr = matchService.findAllByPhase(phase);
            return  ResponseEntity
                    .status(HttpStatus.OK)
                    .body(mr);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
