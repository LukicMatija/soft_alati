package rs.fon.bg.ac.rs.lukic_matija.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.fon.bg.ac.rs.lukic_matija.domain.*;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos.MatchUpdateDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.MatchRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TeamRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TournamentRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private MatchService matchService;

    private Tournament sampleTournament;
    private Team homeTeam;
    private Team awayTeam;
    private Match sampleMatch;

    @BeforeEach
    void setUp() {
        sampleTournament = Tournament.builder().id(10L).name("Lav Kup Srbije").build();
        homeTeam = Team.builder().id(1L).name("Partizan").build();
        awayTeam = Team.builder().id(2L).name("Cukaricki").build();

        sampleMatch = Match.builder()
                .id(100L)
                .scheduleTime(LocalDateTime.now())
                .played(false)
                .phase(MatchPhase.QUARTERFINALS)
                .homePoints(0)
                .awayPoints(0)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .tournament(sampleTournament)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should successfully create a match when all constraints and repositories pass")
    void create_ValidMatch_SavesAndReturnsResponseDto() {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, "QUARTERFINALS", 10L, 1L, 2L, 0, 0);

        when(matchRepository.countByTournamentIdAndPhase(10L, MatchPhase.QUARTERFINALS)).thenReturn(0L);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(awayTeam));
        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(sampleTournament));
        when(matchRepository.save(any(Match.class))).thenReturn(sampleMatch);

        MatchResponseDto result = matchService.create(addDto);

        assertNotNull(result);
        assertEquals("Partizan", result.homeTeamName());
        assertEquals("Cukaricki", result.awayTeamName());
        assertEquals("QUARTERFINALS", result.phase());

        verify(matchRepository).save(any(Match.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when home and away teams have identical IDs")
    void create_SameTeamIds_ThrowsIllegalArgumentException() {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, "QUARTERFINALS", 10L, 1L, 1L, 0, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                matchService.create(addDto)
        );

        assertEquals("Same team for home and away", exception.getMessage());
        verifyNoInteractions(teamRepository, tournamentRepository, matchRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "QUARTERFINALS, 4",
            "SEMIFINALS, 2",
            "FINALS, 1"
    })
    @DisplayName("Should throw IllegalArgumentException when match count for a specific phase is exceeded")
    void create_MaxMatchCountExceeded_ThrowsIllegalArgumentException(String phaseStr, long count) {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, phaseStr, 10L, 1L, 2L, 0, 0);
        MatchPhase phase = MatchPhase.valueOf(phaseStr);

        when(matchRepository.countByTournamentIdAndPhase(10L, phase)).thenReturn(count);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                matchService.create(addDto)
        );

        assertTrue(exception.getMessage().contains("vec ima maksimalan broj meceva"));
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when a team has already played a match in the current phase")
    void create_TeamAlreadyPlayedInCurrentPhase_ThrowsIllegalArgumentException() {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, "SEMIFINALS", 10L, 1L, 2L, 0, 0);

        when(matchRepository.countByTournamentIdAndPhase(10L, MatchPhase.SEMIFINALS)).thenReturn(0L);

        when(matchRepository.findByTournamentIdAndPhaseAndTeamId(10L, MatchPhase.QUARTERFINALS, 1L)).thenReturn(Collections.emptyList());

        when(matchRepository.findByTournamentIdAndPhaseAndTeamId(10L, MatchPhase.SEMIFINALS, 1L))
                .thenReturn(List.of(sampleMatch));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                matchService.create(addDto)
        );

        assertTrue(exception.getMessage().contains("je vec odigrao mec u ovoj fazi"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when a team has not finished its match in the previous phase")
    void create_PreviousPhaseNotPlayed_ThrowsIllegalArgumentException() {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, "SEMIFINALS", 10L, 1L, 2L, 0, 0);

        Match unplayedOldMatch = Match.builder().played(false).build();

        when(matchRepository.countByTournamentIdAndPhase(10L, MatchPhase.SEMIFINALS)).thenReturn(0L);
        when(matchRepository.findByTournamentIdAndPhaseAndTeamId(10L, MatchPhase.SEMIFINALS, 1L)).thenReturn(Collections.emptyList());
        when(matchRepository.findByTournamentIdAndPhaseAndTeamId(10L, MatchPhase.QUARTERFINALS, 1L)).thenReturn(List.of(unplayedOldMatch));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                matchService.create(addDto)
        );

        assertTrue(exception.getMessage().contains("jos uvek nije odigrao mec u fazi QUARTERFINALS"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when home team was eliminated in the previous phase")
    void create_HomeTeamEliminated_ThrowsIllegalArgumentException() {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, "SEMIFINALS", 10L, 1L, 2L, 0, 0);

        Match historicalMatch = Match.builder()
                .played(true)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homePoints(1)
                .awayPoints(2)
                .build();

        when(matchRepository.countByTournamentIdAndPhase(10L, MatchPhase.SEMIFINALS)).thenReturn(0L);
        when(matchRepository.findByTournamentIdAndPhaseAndTeamId(10L, MatchPhase.SEMIFINALS, 1L)).thenReturn(Collections.emptyList());
        when(matchRepository.findByTournamentIdAndPhaseAndTeamId(10L, MatchPhase.QUARTERFINALS, 1L)).thenReturn(List.of(historicalMatch));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                matchService.create(addDto)
        );

        assertTrue(exception.getMessage().contains("je ispao u fazi QUARTERFINALS"));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when home team repository returns empty")
    void create_HomeTeamNotFound_ThrowsEntityNotFoundException() {
        MatchAddDto addDto = new MatchAddDto(LocalDateTime.now(), false, "QUARTERFINALS", 10L, 1L, 2L, 0, 0);

        when(matchRepository.countByTournamentIdAndPhase(10L, MatchPhase.QUARTERFINALS)).thenReturn(0L);
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                matchService.create(addDto)
        );

        assertEquals("Home team doesnt exist", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully update match scores and flip played flag to true")
    void update_MatchExists_UpdatesScoresAndSetsPlayedTrue() {
        MatchUpdateDto updateDto = new MatchUpdateDto(3, 1); // 3:1 pobeda kući
        sampleMatch.setPlayed(false);

        when(matchRepository.findByIdAndTournamentId(100L, 10L)).thenReturn(Optional.of(sampleMatch));
        when(matchRepository.save(sampleMatch)).thenReturn(sampleMatch);

        MatchResponseDto result = matchService.update(10L, 100L, updateDto);

        assertNotNull(result);
        assertTrue(sampleMatch.isPlayed());
        assertEquals(3, sampleMatch.getHomePoints());
        assertEquals(1, sampleMatch.getAwayPoints());
        assertEquals("Pobednik je domaci tim", result.result());

        verify(matchRepository).save(sampleMatch);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating a match that does not exist")
    void update_MatchNotFound_ThrowsEntityNotFoundException() {
        MatchUpdateDto updateDto = new MatchUpdateDto(2, 2);

        when(matchRepository.findByIdAndTournamentId(999L, 10L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                matchService.update(10L, 999L, updateDto)
        );

        assertEquals("Match doesnt exist", exception.getMessage());
        verify(matchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return a list of mapped match response DTOs for a specific valid phase")
    void findAllByPhase_ValidPhase_ReturnsMappedList() {
        sampleMatch.setPlayed(true);
        sampleMatch.setHomePoints(1);
        sampleMatch.setAwayPoints(1);

        when(matchRepository.findAllByPhase(MatchPhase.FINALS)).thenReturn(List.of(sampleMatch));

        List<MatchResponseDto> result = matchService.findAllByPhase("FINALS");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mec je završen nereseno!", result.get(0).result());
        verify(matchRepository).findAllByPhase(MatchPhase.FINALS);
    }
}