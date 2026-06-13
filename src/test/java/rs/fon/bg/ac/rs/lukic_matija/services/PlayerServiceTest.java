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
import rs.fon.bg.ac.rs.lukic_matija.domain.Player;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos.PlayerAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos.PlayerResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.PlayerRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerService playerService;

    private Team sampleTeam;
    private Player samplePlayer;
    private PlayerAddDto sampleAddDto;

    @BeforeEach
    void setUp() {
        sampleTeam = Team.builder()
                .id(1L)
                .name("Partizan")
                .build();

        samplePlayer = Player.builder()
                .id(22L)
                .firstName("Sasa")
                .lastName("Ilic")
                .position("Midfielder")
                .jerseyNumber(22)
                .injured(false)
                .team(sampleTeam)
                .build();

        sampleAddDto = new PlayerAddDto(1L, "Sasa", "Ilic", "Midfielder", 22, false);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should successfully create a player when the specified team exists")
    void createPlayer_ValidTeam_SavesAndReturnsResponseDto() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(sampleTeam));
        when(playerRepository.save(any(Player.class))).thenReturn(samplePlayer);

        PlayerResponseDto result = playerService.createPlayer(sampleAddDto);

        assertNotNull(result);
        assertEquals("Sasa", result.firstName());
        assertEquals("Ilic", result.lastName());
        assertEquals("Midfielder", result.position());
        assertEquals(22, result.jerseyNumber());
        assertEquals("Partizan", result.teamName());
        assertEquals("Zdrav", result.injured());

        verify(teamRepository).findById(1L);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during creation when target team is missing")
    void createPlayer_TeamNotFound_ThrowsEntityNotFoundException() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                playerService.createPlayer(sampleAddDto)
        );

        assertEquals("Team doesnt exist", exception.getMessage());
        verifyNoInteractions(playerRepository);
    }

    @Test
    @DisplayName("Should successfully update player injury status when player exists within team")
    void updateInjury_PlayerExistsInTeam_UpdatesAndSaves() {
        Long playerId = 22L;
        Long teamId = 1L;
        boolean newInjuryStatus = true;

        when(playerRepository.findByIdAndTeamId(playerId, teamId)).thenReturn(Optional.of(samplePlayer));
        when(playerRepository.save(samplePlayer)).thenReturn(samplePlayer);

        PlayerResponseDto result = playerService.updateInjury(playerId, teamId, newInjuryStatus);

        assertNotNull(result);
        assertTrue(samplePlayer.isInjured());
        assertEquals("Povredjen", result.injured()); // Provera da li DTO ispravno vraća string na osnovu novog stanja entiteta

        verify(playerRepository).findByIdAndTeamId(playerId, teamId);
        verify(playerRepository).save(samplePlayer);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating injury for a player missing from team")
    void updateInjury_PlayerNotFoundInTeam_ThrowsEntityNotFoundException() {
        Long missingPlayerId = 999L;
        Long teamId = 1L;

        when(playerRepository.findByIdAndTeamId(missingPlayerId, teamId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                playerService.updateInjury(missingPlayerId, teamId, true)
        );

        assertEquals("Player doesnt exist in team", exception.getMessage());
        verify(playerRepository, never()).save(any(Player.class));
    }
}