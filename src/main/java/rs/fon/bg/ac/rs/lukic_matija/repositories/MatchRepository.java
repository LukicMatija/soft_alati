package rs.fon.bg.ac.rs.lukic_matija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fon.bg.ac.rs.lukic_matija.domain.Match;
import rs.fon.bg.ac.rs.lukic_matija.domain.MatchPhase;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match,Long> {
    Optional<Match> findByIdAndTournamentId(Long id, Long teamId);
    List<Match> findAllByPhase(MatchPhase phase);
}
