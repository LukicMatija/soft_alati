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
}