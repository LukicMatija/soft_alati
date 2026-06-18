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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Validator validator;
    private City mockCity;
    private List<Player> mockPlayers;
    private List<Match> mockHomeMatches;
    private List<Match> mockAwayMatches;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockCity = Mockito.mock(City.class);
        mockPlayers = Mockito.mock(List.class);
        mockHomeMatches = Mockito.mock(List.class);
        mockAwayMatches = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Partizan, 1945-10-04, 25, 5",
            "Real Madrid, 1902-03-06, 30, 2",
            "Manchester United, 1878-03-05, 18, 12"
    })
    @DisplayName("Should pass validation with valid team profiles")
    void validate_ValidTeams_NoViolations(String name, String dateStr, int wins, int losses) {
        LocalDate foundationDate = LocalDate.parse(dateStr);
        Team team = Team.builder()
                .id(1L)
                .name(name)
                .foundationDate(foundationDate)
                .winsCount(wins)
                .lossesCount(losses)
                .city(mockCity)
                .players(mockPlayers)
                .homeMatches(mockHomeMatches)
                .awayMatches(mockAwayMatches)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok data annotations and entity relations function correctly")
    void testLombokAndAssociations() {
        LocalDate date = LocalDate.of(1945, 10, 4);
        Team team = new Team();
        team.setId(4L);
        team.setName("Partizan");
        team.setFoundationDate(date);
        team.setWinsCount(50);
        team.setLossesCount(10);
        team.setCity(mockCity);
        team.setPlayers(mockPlayers);
        team.setHomeMatches(mockHomeMatches);
        team.setAwayMatches(mockAwayMatches);

        assertEquals(4L, team.getId());
        assertEquals("Partizan", team.getName());
        assertEquals(date, team.getFoundationDate());
        assertEquals(50, team.getWinsCount());
        assertEquals(10, team.getLossesCount());
        assertEquals(mockCity, team.getCity());
        assertEquals(mockPlayers, team.getPlayers());
        assertEquals(mockHomeMatches, team.getHomeMatches());
        assertEquals(mockAwayMatches, team.getAwayMatches());
    }

    @Test
    @DisplayName("Should correctly evaluate Builder pattern structural mechanics")
    void testTeamBuilderPattern() {
        Team team = Team.builder()
                .id(9L)
                .name("Partizan")
                .winsCount(100)
                .build();

        assertNotNull(team);
        assertEquals(9L, team.getId());
        assertEquals("Partizan", team.getName());
        assertEquals(100, team.getWinsCount());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when team name is null, empty, or blank")
    void validate_InvalidNameBlank_ReturnsConstraintViolations(String invalidName) {
        Team team = Team.builder()
                .id(1L)
                .name(invalidName)
                .foundationDate(LocalDate.now())
                .winsCount(10)
                .lossesCount(5)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Team name cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when team name exceeds 100 characters")
    void validate_NameTooLong_ReturnsConstraintViolations() {
        String longName = "A".repeat(101);

        Team team = Team.builder()
                .id(1L)
                .name(longName)
                .foundationDate(LocalDate.now())
                .winsCount(10)
                .lossesCount(5)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Team name cannot exceed 100 characters"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when foundation date is null")
    void validate_NullFoundationDate_ReturnsConstraintViolations() {
        Team team = Team.builder()
                .id(1L)
                .name("Partizan")
                .foundationDate(null)
                .winsCount(10)
                .lossesCount(5)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Foundation date is required"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    @DisplayName("Should fail validation when wins count is strictly negative")
    void validate_InvalidWinsCount_ReturnsConstraintViolations(int invalidWins) {
        Team team = Team.builder()
                .id(1L)
                .name("Partizan")
                .foundationDate(LocalDate.now())
                .winsCount(invalidWins)
                .lossesCount(5)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Wins count cannot be negative"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -50})
    @DisplayName("Should fail validation when losses count is strictly negative")
    void validate_InvalidLossesCount_ReturnsConstraintViolations(int invalidLosses) {
        Team team = Team.builder()
                .id(1L)
                .name("Partizan")
                .foundationDate(LocalDate.now())
                .winsCount(10)
                .lossesCount(invalidLosses)
                .city(mockCity)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Lose count cannot be negative"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when city relation is null")
    void validate_NullCity_ReturnsConstraintViolations() {
        Team team = Team.builder()
                .id(1L)
                .name("Partizan")
                .foundationDate(LocalDate.now())
                .winsCount(10)
                .lossesCount(5)
                .city(null)
                .build();

        Set<ConstraintViolation<Team>> violations = validator.validate(team);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Team must be associated with a city"));
        assertTrue(hasCorrectMessage);
    }
}