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
import rs.fon.bg.ac.rs.lukic_matija.domain.*;
import rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos.DelegationAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos.DelegationResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.DelegationRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.MatchRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.RefereeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DelegationServiceTest {

    @Mock
    private DelegationRepository delegationRepository;
    @Mock
    private RefereeRepository refereeRepository;
    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private DelegationService delegationService;

    private Match sampleMatch;
    private Referee sampleReferee;
    private Delegation sampleDelegation;
    private DelegationAddDto sampleAddDto;


    @BeforeEach
    void setUp() {
        Tournament tournament = Tournament.builder().name("Lav Kup Srbije").build();
        Team homeTeam = Team.builder().name("Partizan").build();
        Team awayTeam = Team.builder().name("Napredak Krusevac").build();

        sampleMatch = Match.builder()
                .id(100L)
                .tournament(tournament)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();

        sampleReferee = Referee.builder()
                .id(50L)
                .fullName("Milorad Mazic")
                .build();

        sampleDelegation = Delegation.builder()
                .id(1L)
                .refereeRole("Main Referee")
                .officiatingScore(8.5)
                .match(sampleMatch)
                .referee(sampleReferee)
                .build();

        sampleAddDto = new DelegationAddDto("Main Referee", 8.5, 100L, 50L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should successfully create delegation when both match and referee exist")
    void create_ValidDependencies_SavesAndReturnsResponseDto() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(sampleMatch));
        when(refereeRepository.findById(50L)).thenReturn(Optional.of(sampleReferee));
        when(delegationRepository.save(any(Delegation.class))).thenReturn(sampleDelegation);

        DelegationResponseDto result = delegationService.create(sampleAddDto);

        assertNotNull(result);
        assertEquals("Main Referee", result.role());
        assertEquals(8.5, result.score());
        assertEquals("Milorad Mazic", result.refereeName());
        assertEquals("Partizan", result.homeTean());
        assertEquals("Napredak Krusevac", result.awayTeam());
        assertEquals("Lav Kup Srbije", result.tourName());

        verify(matchRepository).findById(100L);
        verify(refereeRepository).findById(50L);
        verify(delegationRepository).save(any(Delegation.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during creation when match is missing")
    void create_MatchNotFound_ThrowsEntityNotFoundException() {
        when(matchRepository.findById(100L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                delegationService.create(sampleAddDto)
        );

        assertEquals("Match doesnt exists", exception.getMessage());
        verifyNoInteractions(refereeRepository, delegationRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during creation when referee is missing")
    void create_RefereeNotFound_ThrowsEntityNotFoundException() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(sampleMatch));
        when(refereeRepository.findById(50L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                delegationService.create(sampleAddDto)
        );

        assertEquals("Referee doesnt exists", exception.getMessage());
        verify(delegationRepository, never()).save(any(Delegation.class));
    }

    @Test
    @DisplayName("Should successfully update officiating score when delegation exists")
    void updateScore_DelegationExists_UpdatesAndSaves() {
        Long delegationId = 1L;
        double newScore = 9.2;

        when(delegationRepository.findById(delegationId)).thenReturn(Optional.of(sampleDelegation));
        when(delegationRepository.save(sampleDelegation)).thenReturn(sampleDelegation);

        DelegationResponseDto result = delegationService.updateScore(delegationId, newScore);

        assertNotNull(result);
        assertEquals(newScore, sampleDelegation.getOfficiatingScore());
        assertEquals(newScore, result.score());
        verify(delegationRepository).findById(delegationId);
        verify(delegationRepository).save(sampleDelegation);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException during score update when delegation is missing")
    void updateScore_DelegationNotFound_ThrowsEntityNotFoundException() {
        Long missingId = 999L;
        when(delegationRepository.findById(missingId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                delegationService.updateScore(missingId, 7.5)
        );

        assertEquals("Delegation doesnt exists", exception.getMessage());
        verify(delegationRepository, never()).save(any(Delegation.class));
    }
}