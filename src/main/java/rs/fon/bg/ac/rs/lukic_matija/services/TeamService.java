package rs.fon.bg.ac.rs.lukic_matija.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fon.bg.ac.rs.lukic_matija.domain.City;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.CityRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;

/**
 * Service for managing football teams.
 * Handles team registration and ensures that every team is associated
 * with an existing city.
 * @author Matija Lukic
 */
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final CityRepository cityRepository;

    public TeamService(TeamRepository teamRepository, CityRepository cityRepository) {
        this.teamRepository = teamRepository;
        this.cityRepository = cityRepository;
    }
    /**
     * Creates a new team and associates it with an existing city.
     *
     * @param teamAdd TeamAddDto data transfer object containing team information
     * and the identifier of the city the team represents.
     * @return TeamResponseDto containing the details of the successfully created team.
     * @throws jakarta.persistence.EntityNotFoundException If the specified city cannot be found.
     */
    @Transactional
    public TeamResponseDto create(TeamAddDto teamAdd){
        City c = cityRepository.findById(teamAdd.cityId())
                .orElseThrow(()-> new EntityNotFoundException("City doesnt exist"));
        Team t = teamAdd.toEntity();
        t.setCity(c);
        return TeamResponseDto.fromEntity(teamRepository.save(t));
    }
}
