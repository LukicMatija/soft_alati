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

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }
    @Transactional
    public PlayerResponseDto createPlayer(PlayerAddDto pAdd){
        Team t = teamRepository.findById(pAdd.teamId())
                .orElseThrow(()-> new EntityNotFoundException("Team doesnt exist"));
        Player p = pAdd.toEntity();
        p.setTeam(t);
        return PlayerResponseDto.fromEntity(playerRepository.save(p));
    }
    @Transactional
    public PlayerResponseDto updateInjury(Long id,Long teamId, boolean injury ){
        Player p = playerRepository.findByIdAndTeamId(id, teamId)
                .orElseThrow(()-> new EntityNotFoundException("Player doesnt exist in team"));
        p.setInjured(injury);
        return PlayerResponseDto.fromEntity(playerRepository.save(p));
    }
}
