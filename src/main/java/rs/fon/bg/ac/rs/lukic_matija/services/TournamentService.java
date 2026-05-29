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
        City city = cityRepository.findById(tournamentAdd.cityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        Tournament t = tournamentAdd.toEntity();
        t.setCity(city);
        return TournamentResponseDto.fromEntity(tournamentRepository.save(t));
    }

    @Transactional
    public TournamentResponseDto updateTournament(Long id, TournamentAddDto tournamentUpdate){
        Tournament t = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        City city = cityRepository.findById(tournamentUpdate.cityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        t.setCity(city);
        t.setName(tournamentUpdate.name());
        t.setPrizePool(tournamentUpdate.prizePool());
        return TournamentResponseDto.fromEntity(tournamentRepository.save(t));
    }

    @Transactional
    public void deleteTournament(Long id){
        Tournament t = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        tournamentRepository.delete(t);
    }
}
