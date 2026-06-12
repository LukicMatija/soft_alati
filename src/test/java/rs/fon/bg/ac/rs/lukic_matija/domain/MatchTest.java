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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Validator validator;
    private Tournament mockTournament;
    private Team mockHomeTeam;
    private Team mockAwayTeam;
    private List<Delegation> mockDelegations;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockTournament = Mockito.mock(Tournament.class);
        mockHomeTeam = Mockito.mock(Team.class);
        mockAwayTeam = Mockito.mock(Team.class);
        mockDelegations = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "QUARTERFINALS, 85, 82, true",
            "SEMIFINALS, 90, 95, true",
            "FINALS, 0, 0, false" // Primer kada meč tek treba da se odigra
    })
    @DisplayName("Should pass validation with perfectly structured match parameters")
    void validate_ValidMatches_NoViolations(String phaseStr, int homePoints, int awayPoints, boolean played) {
        MatchPhase phase = MatchPhase.valueOf(phaseStr);

        Match match = Match.builder()
                .id(1L)
                .scheduleTime(LocalDateTime.now())
                .played(played)
                .phase(phase)
                .homePoints(homePoints)
                .awayPoints(awayPoints)
                .tournament(mockTournament)
                .homeTeam(mockHomeTeam)
                .awayTeam(mockAwayTeam)
                .delegations(mockDelegations)
                .build();

        Set<ConstraintViolation<Match>> violations = validator.validate(match);
        assertTrue(violations.isEmpty(), "Validation should pass for valid match structures");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    @DisplayName("Should fail validation when home points are strictly negative")
    void validate_InvalidHomePoints_HasViolations(int invalidPoints) {
        Match match = Match.builder()
                .phase(MatchPhase.FINALS)
                .homePoints(invalidPoints)
                .awayPoints(80)
                .build();

        Set<ConstraintViolation<Match>> violations = validator.validate(match);

        assertFalse(violations.isEmpty(), "Validation must fail for negative points");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("homePoints")),
                "Violation should be linked to 'homePoints' property");
    }
    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -50})
    @DisplayName("Should fail validation when away points are strictly negative")
    void validate_InvalidAwayPoints_HasViolations(int invalidPoints) {
        Match match = Match.builder()
                .phase(MatchPhase.SEMIFINALS)
                .homePoints(90)
                .awayPoints(invalidPoints)
                .build();

        Set<ConstraintViolation<Match>> violations = validator.validate(match);

        assertFalse(violations.isEmpty(), "Validation must fail for negative points");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("awayPoints")),
                "Violation should be linked to 'awayPoints' property");
    }

    @Test
    @DisplayName("Should verify Lombok getters, setters, and complex framework bindings")
    void testLombokAndAssociations() {
        LocalDateTime time = LocalDateTime.now();
        Match match = new Match();
        match.setId(7L);
        match.setScheduleTime(time);
        match.setPlayed(true);
        match.setPhase(MatchPhase.FINALS);
        match.setHomePoints(101);
        match.setAwayPoints(99);
        match.setTournament(mockTournament);
        match.setHomeTeam(mockHomeTeam);
        match.setAwayTeam(mockAwayTeam);
        match.setDelegations(mockDelegations);

        assertEquals(7L, match.getId());
        assertEquals(time, match.getScheduleTime());
        assertTrue(match.isPlayed());
        assertEquals(MatchPhase.FINALS, match.getPhase());
        assertEquals(101, match.getHomePoints());
        assertEquals(99, match.getAwayPoints());

        assertEquals(mockTournament, match.getTournament());
        assertEquals(mockHomeTeam, match.getHomeTeam());
        assertEquals(mockAwayTeam, match.getAwayTeam());
        assertEquals(mockDelegations, match.getDelegations());
    }

    @Test
    @DisplayName("Should verify Builder pattern configuration initializes variables correctly")
    void testMatchBuilderPattern() {
        Match match = Match.builder()
                .id(12L)
                .phase(MatchPhase.QUARTERFINALS)
                .played(false)
                .build();

        assertNotNull(match);
        assertEquals(12L, match.getId());
        assertEquals(MatchPhase.QUARTERFINALS, match.getPhase());
        assertFalse(match.isPlayed());
    }
}