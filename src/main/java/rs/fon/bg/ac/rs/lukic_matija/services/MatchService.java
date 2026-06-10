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

/**
 * Service for managing tournament matches.
 * Handles the lifecycle of a match including creation with complex elimination
 * validations, score updates, and phase-based retrievals.
 * @author Matija Lukic
 */
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


    /**
     * Creates a new match within a tournament after validating team rules and phase constraints.
     * Enforces that a team cannot play against itself, that the maximum match count for the phase
     * is not exceeded, and that neither team has been eliminated in previous phases.
     * @param matchAdd MatchAddDto data transfer object containing tournament, team, and phase details.
     * @return MatchResponseDto containing the information of the newly created match.
     * @throws java.lang.IllegalArgumentException If the home and away teams are identical, the maximum match limit for the phase is reached, or a team has already been eliminated.
     * @throws jakarta.persistence.EntityNotFoundException If the home team, away team, or tournament cannot be found.
     */
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
    /**
     * Updates the result of an existing match and marks it as played.
     * Stores the final score for both teams participating in the match.
     *
     * @param tournamentId unique identifier of the tournament to which the match belongs.
     * @param id unique identifier of the match to be updated.
     * @param matchUpdate MatchUpdateDto containing the updated home and away team scores.
     * @return MatchResponseDto containing the updated match information.
     * @throws jakarta.persistence.EntityNotFoundException If the specified match does not exist within the given tournament.
     */
    @Transactional
    public MatchResponseDto update(Long tournamentId, Long id, MatchUpdateDto matchUpdate){
        Match m = matchRepository.findByIdAndTournamentId(id, tournamentId)
                .orElseThrow(()-> new EntityNotFoundException("Match doesnt exist"));
        m.setHomePoints(matchUpdate.homePoints());
        m.setAwayPoints(matchUpdate.awayPoints());
        m.setPlayed(true);
        return MatchResponseDto.fromEntity(matchRepository.save(m));
    }
    /**
     * Retrieves all matches belonging to a specific tournament phase.
     *
     * @param phase tournament phase name (QUARTERFINALS, SEMIFINALS, or FINALS).
     * @return List of MatchResponseDto objects representing all matches found in the specified phase.
     * @throws java.lang.IllegalArgumentException If the provided phase value does not correspond to a valid MatchPhase.
     */
    @Transactional
    public List<MatchResponseDto> findAllByPhase(String phase){
        List<MatchResponseDto> responseList= new ArrayList<>();
        List<Match> matches = matchRepository.findAllByPhase(MatchPhase.valueOf(phase));
        for (Match m : matches) {
            responseList.add(MatchResponseDto.fromEntity(m));
        }
        return responseList;
    }
    /**
     * Validates that the maximum number of matches allowed for a tournament phase
     * has not been exceeded.
     * The allowed number of matches depends on the elimination phase:
     * quarterfinals (4), semifinals (2), and finals (1).
     *
     * @param tournamentId unique identifier of the tournament.
     * @param faza phase for which the match count validation is performed.
     * @throws java.lang.IllegalArgumentException If the phase already contains the maximum permitted number of matches.
     */
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
    /**
     * Verifies whether a team is eligible to participate in the specified tournament phase.
     * The method checks if the team has been eliminated in the previous phase or if
     * its previous-phase match has not yet been completed.
     *
     * @param tournamentId unique identifier of the tournament.
     * @param teamId unique identifier of the team being validated.
     * @param trenutnaFaza phase in which the team is attempting to participate.
     * @throws java.lang.IllegalArgumentException If the team has not completed the previous phase
     *         or has already been eliminated from the tournament.
     */
    private void proveriDaLiJeTimIspao(Long tournamentId, Long teamId, MatchPhase trenutnaFaza) {
        MatchPhase prethodnaFaza = vratiPrethodnuFazu(trenutnaFaza);
        if (prethodnaFaza == null) {
            return;
        }

        List<Match> prethodniMecevi = matchRepository.findByTournamentIdAndPhaseAndTeamId(tournamentId, prethodnaFaza, teamId);
        List<Match> trenutnaFazaMecevi = matchRepository.findByTournamentIdAndPhaseAndTeamId(tournamentId, trenutnaFaza, teamId);
        if(!trenutnaFazaMecevi.isEmpty()){
            throw new IllegalArgumentException("Tim sa ID " + teamId + " je vec odigrao mec u ovoj fazi.");
        }

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
    /**
     * Determines the tournament phase immediately preceding the provided phase.
     *
     * @param trenutnaFaza current tournament phase.
     * @return MatchPhase representing the previous phase, or null if the current phase
     *         is QUARTERFINALS and no earlier phase exists.
     */
    private MatchPhase vratiPrethodnuFazu(MatchPhase trenutnaFaza) {
        return switch (trenutnaFaza) {
            case FINALS -> MatchPhase.SEMIFINALS;
            case SEMIFINALS -> MatchPhase.QUARTERFINALS;
            case QUARTERFINALS -> null;
        };
    }
}