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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    private Validator validator;
    private List<Delegation> mockDelegations;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockDelegations = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Srdjan Jovanovic, FIFA Badge, 12",
            "Milorad Mazic, Elite Badge, 20",
            "Danilo Grujic, National License, 8"
    })
    @DisplayName("Should pass validation with valid referee profiles")
    void validate_ValidReferees_NoViolations(String fullName, String license, int experienceLevel) {
        Referee referee = Referee.builder()
                .id(1L)
                .fullName(fullName)
                .license(license)
                .experienceLevel(experienceLevel)
                .delegations(mockDelegations)
                .build();

        Set<ConstraintViolation<Referee>> violations = validator.validate(referee);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok data annotations and entity relations function correctly")
    void testLombokAndAssociations() {
        Referee referee = new Referee();
        referee.setId(5L);
        referee.setFullName("Nenad Minakovic");
        referee.setLicense("FIFA Badge");
        referee.setExperienceLevel(6);
        referee.setDelegations(mockDelegations);

        assertEquals(5L, referee.getId());
        assertEquals("Nenad Minakovic", referee.getFullName());
        assertEquals("FIFA Badge", referee.getLicense());
        assertEquals(6, referee.getExperienceLevel());
        assertEquals(mockDelegations, referee.getDelegations());
    }

    @Test
    @DisplayName("Should correctly evaluate Builder pattern structural mechanics")
    void testRefereeBuilderPattern() {
        Referee referee = Referee.builder()
                .id(10L)
                .fullName("Novak Simovic")
                .experienceLevel(10)
                .build();

        assertNotNull(referee);
        assertEquals(10L, referee.getId());
        assertEquals("Novak Simovic", referee.getFullName());
        assertEquals(10, referee.getExperienceLevel());
    }
}