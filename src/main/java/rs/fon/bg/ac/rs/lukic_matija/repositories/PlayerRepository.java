package rs.fon.bg.ac.rs.lukic_matija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fon.bg.ac.rs.lukic_matija.domain.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findByIdAndTeamId(Long id, Long teamId);
}
