package rs.fon.bg.ac.rs.lukic_matija.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    private Validator validator;
    private City mockCity;
    private List<Sponsor> mockSponsors;
    private List<Match> mockMatches;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockCity = Mockito.mock(City.class);
        mockSponsors = Mockito.mock(List.class);
        mockMatches = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Kup Radivoja Koraca, 50000.0",
            "Lav Kup Srbije, 120000.50",
            "Letnji Turnir Beograd, 0.1"
    })
    @DisplayName("Should pass validation with valid tournament profiles")
    void validate_ValidTournaments_NoViolations(String name, double prizePool) {
        Tournament tournament = Tournament.builder()
                .id(1L)
                .name(name)
                .prizePool(prizePool)
                .city(mockCity)
                .sponsors(mockSponsors)
                .matches(mockMatches)
                .build();

        Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok data annotations and entity relations function correctly")
    void testLombokAndAssociations() {
        Tournament tournament = new Tournament();
        tournament.setId(3L);
        tournament.setName("Zimski Kup");
        tournament.setPrizePool(85000.0);
        tournament.setCity(mockCity);
        tournament.setSponsors(mockSponsors);
        tournament.setMatches(mockMatches);

        assertEquals(3L, tournament.getId());
        assertEquals("Zimski Kup", tournament.getName());
        assertEquals(85000.0, tournament.getPrizePool());
        assertEquals(mockCity, tournament.getCity());
        assertEquals(mockSponsors, tournament.getSponsors());
        assertEquals(mockMatches, tournament.getMatches());
    }

    @Test
    @DisplayName("Should correctly evaluate Builder pattern structural mechanics")
    void testTournamentBuilderPattern() {
        Tournament tournament = Tournament.builder()
                .id(7L)
                .name("Trofej Beograda")
                .prizePool(30000.0)
                .build();

        assertNotNull(tournament);
        assertEquals(7L, tournament.getId());
        assertEquals("Trofej Beograda", tournament.getName());
        assertEquals(30000.0, tournament.getPrizePool());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when tournament name is null, empty, or blank")
    void validate_InvalidNameBlank_ReturnsConstraintViolations(String invalidName) {
        Tournament tournament = Tournament.builder()
                .id(1L)
                .name(invalidName)
                .prizePool(50000.0)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Tournament name cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when tournament name exceeds 150 characters")
    void validate_NameTooLong_ReturnsConstraintViolations() {
        String longName = "T".repeat(151);

        Tournament tournament = Tournament.builder()
                .id(1L)
                .name(longName)
                .prizePool(50000.0)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Tournament name cannot exceed 150 characters"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.009, -100.50, -5.0})
    @DisplayName("Should fail validation when prize pool is less than 0.01")
    void validate_InvalidPrizePool_ReturnsConstraintViolations(double invalidPrizePool) {
        Tournament tournament = Tournament.builder()
                .id(1L)
                .name("Kup")
                .prizePool(invalidPrizePool)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Prize pool must be greater than 0"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when city relation is null")
    void validate_NullCity_ReturnsConstraintViolations() {
        Tournament tournament = Tournament.builder()
                .id(1L)
                .name("Kup")
                .prizePool(50000.0)
                .city(null)
                .build();

        Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Tournament must be associated with a city"));
        assertTrue(hasCorrectMessage);
    }
}