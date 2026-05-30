package rs.fon.bg.ac.rs.lukic_matija.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.fon.bg.ac.rs.lukic_matija.domain.City;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.CityRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final CityRepository cityRepository;

    public TeamService(TeamRepository teamRepository, CityRepository cityRepository) {
        this.teamRepository = teamRepository;
        this.cityRepository = cityRepository;
    }

    public TeamResponseDto create(TeamAddDto teamAdd){
        City c = cityRepository.findById(teamAdd.cityId())
                .orElseThrow(()-> new EntityNotFoundException("City doesnt exist"));
        Team t = teamAdd.toEntity();
        t.setCity(c);
        return TeamResponseDto.fromEntity(teamRepository.save(t));
    }
}
