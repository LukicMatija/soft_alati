package rs.fon.bg.ac.rs.lukic_matija.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fon.bg.ac.rs.lukic_matija.domain.Match;
import rs.fon.bg.ac.rs.lukic_matija.domain.MatchPhase;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchUpdateDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.MatchRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TournamentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository matchRepository, TournamentRepository tournamentRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public MatchResponseDto create(MatchAddDto matchAdd){
        Team homeTeam = teamRepository.findById(matchAdd.homeTeamId())
                .orElseThrow(()-> new EntityNotFoundException("Home team doesnt exist"));
        Team awayTeam = teamRepository.findById(matchAdd.awayTeamId())
                .orElseThrow(()-> new EntityNotFoundException("Away team doesnt exist"));
        Tournament tournament = tournamentRepository.findById(matchAdd.tournamentId())
                .orElseThrow(()-> new EntityNotFoundException("Tournament doesnt exist"));
        Match m = matchAdd.toEntity();
        m.setHomeTeam(homeTeam);
        m.setAwayTeam(awayTeam);
        m.setTournament(tournament);
        return MatchResponseDto.fromEntity(matchRepository.save(m));
    }
    @Transactional
    public MatchResponseDto update(Long tournamentId, Long id, MatchUpdateDto matchUpdate){
        Match m = matchRepository.findByIdAndTournamentId(id, tournamentId)
                .orElseThrow(()-> new EntityNotFoundException("Match doesnt exist"));
        m.setHomePoints(matchUpdate.homePoints());
        m.setAwayPoints(matchUpdate.awayPoints());
        m.setPlayed(true);
        return MatchResponseDto.fromEntity(matchRepository.save(m));
    }
    @Transactional
    public List<MatchResponseDto> findAllByPhase(String phase){
        List<MatchResponseDto> responseList= new ArrayList<>();
        List<Match> matches = matchRepository.findAllByPhase(MatchPhase.valueOf(phase));
        for (Match m : matches) {
            responseList.add(MatchResponseDto.fromEntity(m));
        }
        return responseList;
    }
}