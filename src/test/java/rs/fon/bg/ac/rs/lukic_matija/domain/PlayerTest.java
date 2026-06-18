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

import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Validator validator;
    private Team mockTeam;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockTeam = Mockito.mock(Team.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Aleksandar, Mitrovic, Striker, 9, false",
            "Sasa, Zdjelar, Central Midfielder, 16, false",
            "Mateus, Saldanha, Forward, 11, true"
    })
    @DisplayName("Should pass validation with perfectly structured player profiles")
    void validate_ValidPlayers_NoViolations(String firstName, String lastName, String position, int jerseyNumber, boolean injured) {
        Player player = Player.builder()
                .id(1L)
                .firstName(firstName)
                .lastName(lastName)
                .position(position)
                .jerseyNumber(jerseyNumber)
                .injured(injured)
                .team(mockTeam)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);
        assertTrue(violations.isEmpty(), "Validation should pass for correct domain models");
    }

    @Test
    @DisplayName("Should verify Lombok data annotations and entity relations function correctly")
    void testLombokAndAssociations() {
        Player player = new Player();
        player.setId(10L);
        player.setFirstName("Igor");
        player.setLastName("Duljaj");
        player.setPosition("Defensive Midfielder");
        player.setJerseyNumber(22);
        player.setInjured(false);
        player.setTeam(mockTeam);

        assertEquals(10L, player.getId());
        assertEquals("Igor", player.getFirstName());
        assertEquals("Duljaj", player.getLastName());
        assertEquals("Defensive Midfielder", player.getPosition());
        assertEquals(22, player.getJerseyNumber());
        assertFalse(player.isInjured());


        assertEquals(mockTeam, player.getTeam());
    }

    @Test
    @DisplayName("Should correctly evaluate Builder pattern structural mechanics")
    void testPlayerBuilderPattern() {
        Player player = Player.builder()
                .id(33L)
                .firstName("Bibars")
                .lastName("Natcho")
                .jerseyNumber(10)
                .build();

        assertNotNull(player);
        assertEquals(33L, player.getId());
        assertEquals("Bibars", player.getFirstName());
        assertEquals("Natcho", player.getLastName());
        assertEquals(10, player.getJerseyNumber());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when first name is null, empty, or blank")
    void validate_InvalidFirstNameBlank_ReturnsConstraintViolations(String invalidFirstName) {
        Player player = Player.builder()
                .id(1L)
                .firstName(invalidFirstName)
                .lastName("Mitrovic")
                .jerseyNumber(9)
                .team(mockTeam)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("First name cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when first name exceeds 50 characters")
    void validate_FirstNameTooLong_ReturnsConstraintViolations() {
        String longFirstName = "A".repeat(51);

        Player player = Player.builder()
                .id(1L)
                .firstName(longFirstName)
                .lastName("Mitrovic")
                .jerseyNumber(9)
                .team(mockTeam)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("First name cannot exceed 50 characters"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when last name is null, empty, or blank")
    void validate_InvalidLastNameBlank_ReturnsConstraintViolations(String invalidLastName) {
        Player player = Player.builder()
                .id(1L)
                .firstName("Aleksandar")
                .lastName(invalidLastName)
                .jerseyNumber(9)
                .team(mockTeam)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Last name cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when last name exceeds 50 characters")
    void validate_LastNameTooLong_ReturnsConstraintViolations() {
        String longLastName = "A".repeat(51);

        Player player = Player.builder()
                .id(1L)
                .firstName("Aleksandar")
                .lastName(longLastName)
                .jerseyNumber(9)
                .team(mockTeam)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Last name cannot exceed 50 characters"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5, 100, 150})
    @DisplayName("Should fail validation when jersey number is out of bounds")
    void validate_InvalidJerseyNumberBounds_ReturnsConstraintViolations(int invalidJerseyNumber) {
        Player player = Player.builder()
                .id(1L)
                .firstName("Aleksandar")
                .lastName("Mitrovic")
                .jerseyNumber(invalidJerseyNumber)
                .team(mockTeam)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty());

        if (invalidJerseyNumber < 1) {
            boolean hasMinMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Jersey number must be at least 1"));
            assertTrue(hasMinMessage);
        } else {
            boolean hasMaxMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Jersey number cannot be greater than 99"));
            assertTrue(hasMaxMessage);
        }
    }

    @Test
    @DisplayName("Should fail validation when team is null")
    void validate_NullTeam_ReturnsConstraintViolations() {
        Player player = Player.builder()
                .id(1L)
                .firstName("Aleksandar")
                .lastName("Mitrovic")
                .jerseyNumber(9)
                .team(null)
                .build();

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Player must belong to a team"));
        assertTrue(hasCorrectMessage);
    }
}