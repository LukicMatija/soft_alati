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

        // Provera @ManyToOne veze sa timom
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
}