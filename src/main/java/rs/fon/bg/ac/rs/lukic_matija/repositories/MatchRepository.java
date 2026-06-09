package rs.fon.bg.ac.rs.lukic_matija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fon.bg.ac.rs.lukic_matija.domain.Match;
import rs.fon.bg.ac.rs.lukic_matija.domain.MatchPhase;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match,Long> {
    Optional<Match> findByIdAndTournamentId(Long id, Long tourId);
    List<Match> findAllByPhase(MatchPhase phase);
    @Query("SELECT m FROM Match m WHERE m.tournament.id = :tournamentId " +
            "AND m.phase = :phase " +
            "AND (m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId)")
    List<Match> findByTournamentIdAndPhaseAndTeamId(
            @Param("tournamentId") Long tournamentId,
            @Param("phase") MatchPhase phase,
            @Param("teamId") Long teamId
    );
    long countByTournamentIdAndPhase(Long tournamentId, MatchPhase phase);
}
