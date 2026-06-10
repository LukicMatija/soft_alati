package rs.fon.bg.ac.rs.lukic_matija.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fon.bg.ac.rs.lukic_matija.domain.Sponsor;
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;
import rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos.SponsorAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos.SponsorResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.SponsorRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TournamentRepository;

/**
 * Service for managing tournament sponsorships.
 * Handles sponsor registrations and ensures that sponsorship agreements
 * comply with business rules and tournament associations.
 * @author Matija Lukic
 */
@Service
public class SponsorService {
    private final SponsorRepository sponsorRepository;
    private final TournamentRepository tournamentRepository;

    public SponsorService(SponsorRepository sponsorRepository, TournamentRepository tournamentRepository) {
        this.sponsorRepository = sponsorRepository;
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Creates a new sponsorship agreement for a tournament.
     * Validates that the sponsorship budget is greater than zero through DTO validation
     * and ensures that the same company cannot be registered more than once as a sponsor
     * of the same tournament.
     *
     * @param sAdd SponsorAddDto data transfer object containing sponsor company information,
     * sponsorship budget, signing date, and tournament identifier.
     * @return SponsorResponseDto containing the details of the successfully created sponsorship.
     * @throws java.lang.IllegalArgumentException If the company is already registered as a sponsor
     * for the specified tournament.
     * @throws jakarta.persistence.EntityNotFoundException If the specified tournament cannot be found.
     */
    @Transactional
    public SponsorResponseDto create(SponsorAddDto sAdd){
        if (sponsorRepository.existsByCompanyAndTournamentId(sAdd.company(), sAdd.tournamentId())) {
            throw new IllegalArgumentException("Company " + sAdd.company() + " is already sponsor for this tournament");
        }
        Tournament t = tournamentRepository.findById(sAdd.tournamentId())
                .orElseThrow(()-> new EntityNotFoundException("Tournament doesnt exist"));
        Sponsor s = sAdd.toEntity();
        s.setTournament(t);
        return SponsorResponseDto.fromEntity(sponsorRepository.save(s));
    }
}
