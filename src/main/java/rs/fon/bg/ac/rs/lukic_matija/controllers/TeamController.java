package rs.fon.bg.ac.rs.lukic_matija.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.services.TeamService;

@RestController
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("api/teams")
    public ResponseEntity<Object> createTeam(@RequestBody TeamAddDto tAdd){
        try {
            TeamResponseDto tr = teamService.create(tAdd);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(tr);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Create team unsuccessful");
        }
    }
}
