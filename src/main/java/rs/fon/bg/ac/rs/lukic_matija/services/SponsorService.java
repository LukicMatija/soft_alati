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

@Service
public class SponsorService {
    private final SponsorRepository sponsorRepository;
    private final TournamentRepository tournamentRepository;

    public SponsorService(SponsorRepository sponsorRepository, TournamentRepository tournamentRepository) {
        this.sponsorRepository = sponsorRepository;
        this.tournamentRepository = tournamentRepository;
    }

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
