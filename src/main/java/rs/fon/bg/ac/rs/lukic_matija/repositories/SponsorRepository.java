package rs.fon.bg.ac.rs.lukic_matija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fon.bg.ac.rs.lukic_matija.domain.Sponsor;

public interface SponsorRepository extends JpaRepository<Sponsor,Long> {
    boolean existsByCompanyAndTournamentId(String company, Long tournamentId);
}
