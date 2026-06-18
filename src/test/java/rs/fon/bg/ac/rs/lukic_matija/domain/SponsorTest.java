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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SponsorTest {

    private Validator validator;
    private Tournament mockTournament;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockTournament = Mockito.mock(Tournament.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Nike, 50000.0, 2026-01-15",
            "Adidas, 75000.50, 2026-02-20",
            "Puma, 30000.0, 2026-03-10"
    })
    @DisplayName("Should pass validation with valid sponsor profiles")
    void validate_ValidSponsors_NoViolations(String company, double contractBudget, String dateStr) {
        LocalDate signingDate = LocalDate.parse(dateStr);
        Sponsor sponsor = Sponsor.builder()
                .id(1L)
                .company(company)
                .contractBudget(contractBudget)
                .signingDate(signingDate)
                .tournament(mockTournament)
                .build();

        Set<ConstraintViolation<Sponsor>> violations = validator.validate(sponsor);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok data annotations and entity relations function correctly")
    void testLombokAndAssociations() {
        LocalDate date = LocalDate.of(2026, 5, 1);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(8L);
        sponsor.setCompany("Telekom Srbija");
        sponsor.setContractBudget(120000.0);
        sponsor.setSigningDate(date);
        sponsor.setTournament(mockTournament);

        assertEquals(8L, sponsor.getId());
        assertEquals("Telekom Srbija", sponsor.getCompany());
        assertEquals(120000.0, sponsor.getContractBudget());
        assertEquals(date, sponsor.getSigningDate());
        assertEquals(mockTournament, sponsor.getTournament());
    }

    @Test
    @DisplayName("Should correctly evaluate Builder pattern structural mechanics")
    void testSponsorBuilderPattern() {
        Sponsor sponsor = Sponsor.builder()
                .id(14L)
                .company("Mozzart")
                .contractBudget(95000.0)
                .build();

        assertNotNull(sponsor);
        assertEquals(14L, sponsor.getId());
        assertEquals("Mozzart", sponsor.getCompany());
        assertEquals(95000.0, sponsor.getContractBudget());
    }
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should fail validation when company name is null, empty, or blank")
    void validate_InvalidCompanyBlank_ReturnsConstraintViolations(String invalidCompany) {
        Sponsor sponsor = Sponsor.builder()
                .id(1L)
                .company(invalidCompany)
                .contractBudget(50000.0)
                .signingDate(LocalDate.now())
                .tournament(mockTournament)
                .build();

        Set<ConstraintViolation<Sponsor>> violations = validator.validate(sponsor);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Company name cannot be blank"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when company name exceeds 100 characters")
    void validate_CompanyTooLong_ReturnsConstraintViolations() {
        String longCompany = "A".repeat(101);

        Sponsor sponsor = Sponsor.builder()
                .id(1L)
                .company(longCompany)
                .contractBudget(50000.0)
                .signingDate(LocalDate.now())
                .tournament(mockTournament)
                .build();

        Set<ConstraintViolation<Sponsor>> violations = validator.validate(sponsor);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Company name cannot exceed 100 characters"));
        assertTrue(hasCorrectMessage);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.01, -500.0})
    @DisplayName("Should fail validation when contract budget is less than 0.01")
    void validate_InvalidContractBudget_ReturnsConstraintViolations(double invalidBudget) {
        Sponsor sponsor = Sponsor.builder()
                .id(1L)
                .company("Nike")
                .contractBudget(invalidBudget)
                .signingDate(LocalDate.now())
                .tournament(mockTournament)
                .build();

        Set<ConstraintViolation<Sponsor>> violations = validator.validate(sponsor);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Contract budget must be greater than 0"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when signing date is null")
    void validate_NullSigningDate_ReturnsConstraintViolations() {
        Sponsor sponsor = Sponsor.builder()
                .id(1L)
                .company("Nike")
                .contractBudget(50000.0)
                .signingDate(null)
                .tournament(mockTournament)
                .build();

        Set<ConstraintViolation<Sponsor>> violations = validator.validate(sponsor);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Signing date is required"));
        assertTrue(hasCorrectMessage);
    }

    @Test
    @DisplayName("Should fail validation when tournament relation is null")
    void validate_NullTournament_ReturnsConstraintViolations() {
        Sponsor sponsor = Sponsor.builder()
                .id(1L)
                .company("Nike")
                .contractBudget(50000.0)
                .signingDate(LocalDate.now())
                .tournament(null)
                .build();

        Set<ConstraintViolation<Sponsor>> violations = validator.validate(sponsor);

        assertFalse(violations.isEmpty());
        boolean hasCorrectMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Tournament is required"));
        assertTrue(hasCorrectMessage);
    }
}