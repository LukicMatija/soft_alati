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

/**
 * Service for managing football tournaments.
 * Handles creation, updates, and deletion of tournaments while ensuring
 * proper association with cities.
 *
 * @author Matija Lukic
 */
@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final CityRepository cityRepository;

    public TournamentService(TournamentRepository tournamentRepository, CityRepository cityRepository){
        this.tournamentRepository = tournamentRepository;
        this.cityRepository = cityRepository;
    }
    /**
     * Creates a new football tournament and assigns it to an existing city.
     *
     * @param tournamentAdd TournamentAddDto containing tournament details and city identifier.
     * @return TournamentResponseDto containing the created tournament data.
     * @throws jakarta.persistence.EntityNotFoundException If the specified city cannot be found.
     */
    @Transactional
    public TournamentResponseDto createTournament(TournamentAddDto tournamentAdd){
        City city = cityRepository.findById(tournamentAdd.cityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        Tournament t = tournamentAdd.toEntity();
        t.setCity(city);
        return TournamentResponseDto.fromEntity(tournamentRepository.save(t));
    }
    /**
     * Updates an existing football tournament.
     * Updates basic tournament information and reassigns it to a city if needed.
     *
     * @param id unique identifier of the tournament to be updated.
     * @param tournamentUpdate TournamentAddDto containing updated tournament data.
     * @return TournamentResponseDto containing the updated tournament data.
     * @throws jakarta.persistence.EntityNotFoundException If the tournament or city cannot be found.
     */
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
    /**
     * Deletes a football tournament by its unique identifier.
     *
     * @param id unique identifier of the tournament to be deleted.
     * @throws jakarta.persistence.EntityNotFoundException If the tournament with the given id does not exist.
     */
    @Transactional
    public void deleteTournament(Long id){
        Tournament t = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));
        tournamentRepository.delete(t);
    }
}
