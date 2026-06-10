package rs.fon.bg.ac.rs.lukic_matija.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fon.bg.ac.rs.lukic_matija.domain.Player;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos.PlayerAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos.PlayerResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.PlayerRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;

/**
 * Service for managing players within tournament teams.
 * Handles player registration and injury status updates while ensuring
 * that all player-team relationships remain valid.
 * @author Matija Lukic
 */
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }
    /**
     * Creates a new player and assigns them to an existing team.
     * @param pAdd PlayerAddDto data transfer object containing player information and the identifier of the team to which the player belongs.
     * @return PlayerResponseDto containing the details of the successfully created player.
     * @throws jakarta.persistence.EntityNotFoundException If the specified team cannot be found.
     */
    @Transactional
    public PlayerResponseDto createPlayer(PlayerAddDto pAdd){
        Team t = teamRepository.findById(pAdd.teamId())
                .orElseThrow(()-> new EntityNotFoundException("Team doesnt exist"));
        Player p = pAdd.toEntity();
        p.setTeam(t);
        return PlayerResponseDto.fromEntity(playerRepository.save(p));
    }
    /**
     * Updates the injury status of a player belonging to a specific team.
     * @param id unique identifier of the player.
     * @param teamId unique identifier of the team the player belongs to.
     * @param injury new injury status value; true if the player is injured, false otherwise.
     * @return PlayerResponseDto containing the updated player information.
     * @throws jakarta.persistence.EntityNotFoundException If the player does not exist within the specified team.
     */
    @Transactional
    public PlayerResponseDto updateInjury(Long id,Long teamId, boolean injury ){
        Player p = playerRepository.findByIdAndTeamId(id, teamId)
                .orElseThrow(()-> new EntityNotFoundException("Player doesnt exist in team"));
        p.setInjured(injury);
        return PlayerResponseDto.fromEntity(playerRepository.save(p));
    }
}
