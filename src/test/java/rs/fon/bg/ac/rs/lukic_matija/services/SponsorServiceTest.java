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
import rs.fon.bg.ac.rs.lukic_matija.domain.Sponsor;
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;
import rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos.SponsorAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos.SponsorResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.SponsorRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.TournamentRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SponsorServiceTest {

    @Mock
    private SponsorRepository sponsorRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private SponsorService sponsorService;

    private Tournament sampleTournament;
    private Sponsor sampleSponsor;
    private SponsorAddDto sampleAddDto;

    @BeforeEach
    void setUp() {
        sampleTournament = Tournament.builder()
                .id(10L)
                .name("Lav Kup Srbije")
                .build();

        sampleSponsor = Sponsor.builder()
                .id(100L)
                .company("MTS")
                .contractBudget(50000.0)
                .signingDate(LocalDate.now())
                .tournament(sampleTournament)
                .build();

        sampleAddDto = new SponsorAddDto("MTS", 50000.0, LocalDate.now(), 10L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should successfully create a sponsorship agreement when tournament exists and company is unique")
    void create_ValidSponsorship_SavesAndReturnsResponseDto() {
        when(sponsorRepository.existsByCompanyAndTournamentId("MTS", 10L)).thenReturn(false);
        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(sampleTournament));
        when(sponsorRepository.save(any(Sponsor.class))).thenReturn(sampleSponsor);

        SponsorResponseDto result = sponsorService.create(sampleAddDto);

        assertNotNull(result);
        assertEquals("MTS", result.company());
        assertEquals(50000.0, result.budget());
        assertEquals("Lav Kup Srbije", result.tourName());

        verify(sponsorRepository).existsByCompanyAndTournamentId("MTS", 10L);
        verify(tournamentRepository).findById(10L);
        verify(sponsorRepository).save(any(Sponsor.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when company is already registered as a sponsor for this tournament")
    void create_DuplicateCompanySponsor_ThrowsIllegalArgumentException() {
        when(sponsorRepository.existsByCompanyAndTournamentId("MTS", 10L)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sponsorService.create(sampleAddDto)
        );

        assertTrue(exception.getMessage().contains("is already sponsor for this tournament"));

        verify(sponsorRepository).existsByCompanyAndTournamentId("MTS", 10L);
        verifyNoInteractions(tournamentRepository);
        verify(sponsorRepository, never()).save(any(Sponsor.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when registering a sponsor for a missing tournament")
    void create_TournamentNotFound_ThrowsEntityNotFoundException() {
        when(sponsorRepository.existsByCompanyAndTournamentId("MTS", 10L)).thenReturn(false);
        when(tournamentRepository.findById(10L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                sponsorService.create(sampleAddDto)
        );

        assertEquals("Tournament doesnt exist", exception.getMessage());

        verify(sponsorRepository).existsByCompanyAndTournamentId("MTS", 10L);
        verify(tournamentRepository).findById(10L);
        verify(sponsorRepository, never()).save(any(Sponsor.class));
    }
}