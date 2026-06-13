package rs.fon.bg.ac.rs.lukic_matija.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.fon.bg.ac.rs.lukic_matija.domain.City;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos.TeamResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.CityRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private TeamService teamService;

    private City sampleCity;
    private Team sampleTeam;
    private TeamAddDto sampleAddDto;

    @BeforeEach
    void setUp() {
        sampleCity = City.builder()
                .id(1L)
                .name("Beograd")
                .build();

        sampleTeam = Team.builder()
                .id(100L)
                .name("Partizan")
                .foundationDate(LocalDate.of(1945, 10, 4))
                .winsCount(0)
                .lossesCount(0)
                .city(sampleCity)
                .build();

        sampleAddDto = new TeamAddDto("Partizan", LocalDate.of(1945, 10, 4), 1L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should successfully create a team when the specified city exists")
    void create_ValidCity_SavesAndReturnsResponseDto() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(teamRepository.save(any(Team.class))).thenReturn(sampleTeam);

        TeamResponseDto result = teamService.create(sampleAddDto);

        assertNotNull(result);
        assertEquals("Partizan", result.name());
        assertEquals(LocalDate.of(1945, 10, 4), result.foundationDate());
        assertEquals("Beograd", result.cityName());

        verify(cityRepository).findById(1L);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during creation when target city is missing")
    void create_CityNotFound_ThrowsEntityNotFoundException() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                teamService.create(sampleAddDto)
        );

        assertEquals("City doesnt exist", exception.getMessage());

        verify(cityRepository).findById(1L);
        verifyNoInteractions(teamRepository);
    }
}