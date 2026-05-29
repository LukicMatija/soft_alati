package rs.fon.bg.ac.rs.lukic_matija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;

import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
