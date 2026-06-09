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

        MatchPhase phase = MatchPhase.valueOf(matchAdd.phase());
        if (matchAdd.homeTeamId().equals(matchAdd.awayTeamId())) {
            throw new IllegalArgumentException("Same team for home and away");
        }
        validirajMaksimalanBrojMeceva(matchAdd.tournamentId(), phase);

        proveriDaLiJeTimIspao(matchAdd.tournamentId(), matchAdd.homeTeamId(), phase);
        proveriDaLiJeTimIspao(matchAdd.tournamentId(), matchAdd.awayTeamId(), phase);

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
    private void validirajMaksimalanBrojMeceva(Long tournamentId, MatchPhase faza) {
        long brojMecevaUFazi = matchRepository.countByTournamentIdAndPhase(tournamentId, faza);

        int maksimalnoMeceva = switch (faza) {
            case FINALS -> 1;
            case SEMIFINALS -> 2;
            case QUARTERFINALS -> 4;
        };

        if (brojMecevaUFazi >= maksimalnoMeceva) {
            throw new IllegalArgumentException("Faza " + faza + " na ovom turniru vec ima maksimalan broj meceva (" + maksimalnoMeceva + ")!");
        }
    }

    private void proveriDaLiJeTimIspao(Long tournamentId, Long teamId, MatchPhase trenutnaFaza) {
        MatchPhase prethodnaFaza = vratiPrethodnuFazu(trenutnaFaza);
        if (prethodnaFaza == null) {
            return;
        }

        List<Match> prethodniMecevi = matchRepository.findByTournamentIdAndPhaseAndTeamId(tournamentId, prethodnaFaza, teamId);

        for (Match stariMec : prethodniMecevi) {
            if (!stariMec.isPlayed()) {
                throw new IllegalArgumentException("Tim sa ID " + teamId + " jos uvek nije odigrao mec u fazi " + prethodnaFaza);
            }

            boolean jeDomacin = stariMec.getHomeTeam().getId().equals(teamId);

            if (jeDomacin && stariMec.getHomePoints() < stariMec.getAwayPoints()) {
                throw new IllegalArgumentException("Tim " + stariMec.getHomeTeam().getName() + " je ispao u fazi " + prethodnaFaza);
            }
            if (!jeDomacin && stariMec.getAwayPoints() < stariMec.getHomePoints()) {
                throw new IllegalArgumentException("Tim " + stariMec.getAwayTeam().getName() + " je ispao u fazi " + prethodnaFaza);
            }
        }
    }

    private MatchPhase vratiPrethodnuFazu(MatchPhase trenutnaFaza) {
        return switch (trenutnaFaza) {
            case FINALS -> MatchPhase.SEMIFINALS;
            case SEMIFINALS -> MatchPhase.QUARTERFINALS;
            case QUARTERFINALS -> null;
        };
    }
}