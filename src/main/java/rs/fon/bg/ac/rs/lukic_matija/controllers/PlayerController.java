package rs.fon.bg.ac.rs.lukic_matija.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fon.bg.ac.rs.lukic_matija.domain.Player;
import rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos.PlayerAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos.PlayerResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.services.PlayerService;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("api/players")
    public ResponseEntity<Object> create(@RequestBody PlayerAddDto pAdd){
        try {
            PlayerResponseDto pr = playerService.createPlayer(pAdd);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(pr);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Create player unsuccessful");
        }

    }

    @PutMapping("api/players/{teamId}/{id}")
    public ResponseEntity<Object> updateInjury(@PathVariable Long teamId, @RequestParam boolean injury, @PathVariable Long id){
        try {
            PlayerResponseDto pr = playerService.updateInjury(id,teamId,injury);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(pr);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Update player unsuccessful");
        }
    }
}
