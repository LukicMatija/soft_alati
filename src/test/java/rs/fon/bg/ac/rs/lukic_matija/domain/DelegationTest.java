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
}