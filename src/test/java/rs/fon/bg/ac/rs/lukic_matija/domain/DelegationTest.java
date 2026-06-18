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

class DelegationTest {

    private Validator validator;
    private Referee mockReferee;
    private Match mockMatch;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockReferee = Mockito.mock(Referee.class);
        mockMatch = Mockito.mock(Match.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "MAIN_REFEREE, 8.5",
            "ASSISTANT_REFEREE, 9.0",
            "FOURTH_OFFICIAL, 7.2",
            "VAR_REFEREE, 10.0"
    })
    @DisplayName("Should pass validation with valid role and scoring parameters")
    void validate_ValidDelegation_NoViolations(String role, double score) {
        Delegation delegation = Delegation.builder()
                .id(1L)
                .refereeRole(role)
                .officiatingScore(score)
                .referee(mockReferee)
                .match(mockMatch)
                .build();

        Set<ConstraintViolation<Delegation>> violations = validator.validate(delegation);
        assertTrue(violations.isEmpty());
    }
    @Test
    @DisplayName("Should verify Lombok mappings and complex object associations efficiently")
    void testLombokAndAssociations() {
        Delegation delegation = new Delegation();
        delegation.setId(15L);
        delegation.setRefereeRole("MAIN_REFEREE");
        delegation.setOfficiatingScore(8.7);
        delegation.setReferee(mockReferee);
        delegation.setMatch(mockMatch);

        assertEquals(15L, delegation.getId());
        assertEquals("MAIN_REFEREE", delegation.getRefereeRole());
        assertEquals(8.7, delegation.getOfficiatingScore());

        assertEquals(mockReferee, delegation.getReferee());
        assertEquals(mockMatch, delegation.getMatch());
    }
    @Test
    @DisplayName("Should successfully compile and verify Builder pattern initialization")
    void testDelegationBuilderPattern() {
        Delegation delegation = Delegation.builder()
                .id(55L)
                .refereeRole("LINESMAN")
                .officiatingScore(6.5)
                .build();

        assertNotNull(delegation);
        assertEquals(55L, delegation.getId());
        assertEquals("LINESMAN", delegation.getRefereeRole());
        assertEquals(6.5, delegation.getOfficiatingScore());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when referee role is null, empty, or blank")
    void validate_InvalidRefereeRoleBlank_ReturnsConstraintViolations(String invalidRole) {
        Delegation delegation = Delegation.builder()
                .id(1L)
                .refereeRole(invalidRole)
                .officiatingScore(8.5)
                .referee(mockReferee)
                .match(mockMatch)
                .build();

        Set<ConstraintViolation<Delegation>> violations = validator.validate(delegation);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Referee role cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when referee role exceeds 50 characters")
    void validate_RefereeRoleTooLong_ReturnsConstraintViolations() {
        String longRole = "R".repeat(51);

        Delegation delegation = Delegation.builder()
                .id(1L)
                .refereeRole(longRole)
                .officiatingScore(8.5)
                .referee(mockReferee)
                .match(mockMatch)
                .build();

        Set<ConstraintViolation<Delegation>> violations = validator.validate(delegation);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Referee role cannot exceed 50 characters"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -5.0, 10.01, 15.5})
    @DisplayName("Should fail validation when officiating score is out of bounds")
    void validate_InvalidOfficiatingScoreBounds_ReturnsConstraintViolations(double invalidScore) {
        Delegation delegation = Delegation.builder()
                .id(1L)
                .refereeRole("MAIN_REFEREE")
                .officiatingScore(invalidScore)
                .referee(mockReferee)
                .match(mockMatch)
                .build();

        Set<ConstraintViolation<Delegation>> violations = validator.validate(delegation);

        assertFalse(violations.isEmpty());

        if (invalidScore < 0.00) {
            boolean hasMinMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Officiating score cannot be negative"));
            assertTrue(hasMinMessage);
        } else {
            boolean hasMaxMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Officiating score cannot exceed 10.00"));
            assertTrue(hasMaxMessage);
        }
    }
}