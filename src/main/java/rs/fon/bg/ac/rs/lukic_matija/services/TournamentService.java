package rs.fon.bg.ac.rs.lukic_matija.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fon.bg.ac.rs.lukic_matija.domain.City;
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.CityRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TournamentRepository;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final CityRepository cityRepository;

    public TournamentService(TournamentRepository tournamentRepository, CityRepository cityRepository){
        this.tournamentRepository = tournamentRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public TournamentResponseDto createTournament(TournamentAddDto tournamentAdd){
        System.out.println(tournamentAdd);
        City city = cityRepository.findById(tournamentAdd.cityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        Tournament t = tournamentAdd.toEntity();
        t.setCity(city);
        return TournamentResponseDto.fromEntity(tournamentRepository.save(t));
    }
}
