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

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when full name is null, empty, or blank")
    void validate_InvalidFullNameBlank_ReturnsConstraintViolations(String invalidFullName) {
        Referee referee = Referee.builder()
                .id(1L)
                .fullName(invalidFullName)
                .license("FIFA Badge")
                .experienceLevel(10)
                .delegations(mockDelegations)
                .build();

        Set<ConstraintViolation<Referee>> violations = validator.validate(referee);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Full name cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when full name exceeds 100 characters")
    void validate_FullNameTooLong_ReturnsConstraintViolations() {
        String longFullName = "A".repeat(101);

        Referee referee = Referee.builder()
                .id(1L)
                .fullName(longFullName)
                .license("FIFA Badge")
                .experienceLevel(10)
                .delegations(mockDelegations)
                .build();

        Set<ConstraintViolation<Referee>> violations = validator.validate(referee);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Full name cannot exceed 100 characters"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    @DisplayName("Should fail validation when experience level is strictly negative")
    void validate_InvalidExperienceLevel_ReturnsConstraintViolations(int invalidExperience) {
        Referee referee = Referee.builder()
                .id(1L)
                .fullName("Srdjan Jovanovic")
                .license("FIFA Badge")
                .experienceLevel(invalidExperience)
                .delegations(mockDelegations)
                .build();

        Set<ConstraintViolation<Referee>> violations = validator.validate(referee);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Experience level cannot be negative"));
        assertTrue(hasCorrectMessage);
    }
}