package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    private Validator validator;
    private List<Tournament> mockTournaments;
    private List<Team> mockTeams;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockTournaments = Mockito.mock(List.class);
        mockTeams = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Belgrade, 11000, Serbia",
            "Novi Sad, 21000, Serbia",
            "Madrid, 28001, Spain",
            "Paris, 75000, France"
    })
    @DisplayName("Should pass validation with perfectly structured city objects")
    void validate_ValidCities_NoViolations(String name, String postalCode, String country) {
        City city = City.builder()
                .id(1L)
                .name(name)
                .postalCode(postalCode)
                .country(country)
                .tournaments(mockTournaments)
                .teams(mockTeams)
                .build();

        Set<ConstraintViolation<City>> violations = validator.validate(city);
        assertTrue(violations.isEmpty());
    }
    @Test
    @DisplayName("Should verify Lombok mappings and complex structural relationships handle data efficiently")
    void testLombokAndAssociations() {
        City city = new City();
        city.setId(99L);
        city.setName("Smederevska Palanka");
        city.setPostalCode("11420");
        city.setCountry("Serbia");
        city.setTournaments(mockTournaments);
        city.setTeams(mockTeams);

        assertEquals(99L, city.getId());
        assertEquals("Smederevska Palanka", city.getName());
        assertEquals("11420", city.getPostalCode());
        assertEquals("Serbia", city.getCountry());

        assertEquals(mockTournaments, city.getTournaments());
        assertEquals(mockTeams, city.getTeams());
    }

    @Test
    @DisplayName("Should successfully compile and verify Builder pattern initialization")
    void testCityBuilderPattern() {
        City city = City.builder()
                .id(99L)
                .name("Smederevska Palank")
                .postalCode("11420")
                .country("Serbia")
                .build();

        assertNotNull(city);
        assertEquals(99L, city.getId());
        assertEquals("Smederevska Palank", city.getName());
        assertEquals("11420", city.getPostalCode());
        assertEquals("Serbia", city.getCountry());
    }
}