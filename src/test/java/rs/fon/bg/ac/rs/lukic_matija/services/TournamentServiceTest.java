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
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.CityRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TournamentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private TournamentService tournamentService;

    private City sampleCity;
    private Tournament sampleTournament;
    private TournamentAddDto sampleAddDto;

    @BeforeEach
    void setUp() {
        sampleCity = City.builder()
                .id(1L)
                .name("Beograd")
                .build();

        sampleTournament = Tournament.builder()
                .id(10L)
                .name("Lav Kup Srbije")
                .prizePool(250000.0)
                .city(sampleCity)
                .build();

        sampleAddDto = new TournamentAddDto("Lav Kup Srbije", 250000.0, 1L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should successfully create a tournament when the specified city exists")
    void createTournament_ValidCity_SavesAndReturnsResponseDto() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(sampleTournament);

        TournamentResponseDto result = tournamentService.createTournament(sampleAddDto);

        assertNotNull(result);
        assertEquals("Lav Kup Srbije", result.name());
        assertEquals(250000.0, result.prizePool());
        assertEquals("Beograd", result.cityName());

        verify(cityRepository).findById(1L);
        verify(tournamentRepository).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during creation when target city is missing")
    void createTournament_CityNotFound_ThrowsEntityNotFoundException() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                tournamentService.createTournament(sampleAddDto)
        );

        assertEquals("City not found", exception.getMessage());
        verify(cityRepository).findById(1L);
        verifyNoInteractions(tournamentRepository);
    }


    @Test
    @DisplayName("Should successfully update tournament data when tournament and city both exist")
    void updateTournament_ValidTournamentAndCity_UpdatesAndSaves() {
        Long tournamentId = 10L;
        TournamentAddDto updateDto = new TournamentAddDto("Kup Srbije", 300000.0, 1L);

        sampleTournament.setName("Kup Srbije");
        sampleTournament.setPrizePool(300000.0);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(sampleTournament));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(tournamentRepository.save(sampleTournament)).thenReturn(sampleTournament);

        TournamentResponseDto result = tournamentService.updateTournament(tournamentId, updateDto);

        assertNotNull(result);
        assertEquals("Kup Srbije", result.name());
        assertEquals(300000.0, result.prizePool());
        assertEquals("Beograd", result.cityName());

        verify(tournamentRepository).findById(tournamentId);
        verify(cityRepository).findById(1L);
        verify(tournamentRepository).save(sampleTournament);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during update when tournament does not exist")
    void updateTournament_TournamentNotFound_ThrowsEntityNotFoundException() {
        Long missingTournamentId = 999L;

        when(tournamentRepository.findById(missingTournamentId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                tournamentService.updateTournament(missingTournamentId, sampleAddDto)
        );

        assertEquals("Tournament not found", exception.getMessage());
        verify(tournamentRepository).findById(missingTournamentId);
        verifyNoInteractions(cityRepository);
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during update when new city does not exist")
    void updateTournament_CityNotFoundDuringUpdate_ThrowsEntityNotFoundException() {
        Long tournamentId = 10L;
        TournamentAddDto updateDto = new TournamentAddDto("Kup Srbije", 300000.0, 2L); // Nepostojeći cityId 2

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(sampleTournament));
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                tournamentService.updateTournament(tournamentId, updateDto)
        );

        assertEquals("City not found", exception.getMessage());
        verify(tournamentRepository).findById(tournamentId);
        verify(cityRepository).findById(2L);
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }


    @Test
    @DisplayName("Should successfully delete tournament when it exists in database")
    void deleteTournament_TournamentExists_DeletesFromRepository() {
        Long tournamentId = 10L;

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(sampleTournament));
        doNothing().when(tournamentRepository).delete(sampleTournament);

        assertDoesNotThrow(() -> tournamentService.deleteTournament(tournamentId));

        verify(tournamentRepository).findById(tournamentId);
        verify(tournamentRepository).delete(sampleTournament);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during deletion when tournament is missing")
    void deleteTournament_TournamentNotFound_ThrowsEntityNotFoundException() {
        Long missingTournamentId = 999L;

        when(tournamentRepository.findById(missingTournamentId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                tournamentService.deleteTournament(missingTournamentId)
        );

        assertEquals("Tournament not found", exception.getMessage());
        verify(tournamentRepository).findById(missingTournamentId);
        verify(tournamentRepository, never()).delete(any(Tournament.class));
    }
}