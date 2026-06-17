package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

/**
 * Represents the city where tournaments are hosted and teams are located.
 * Serves as a geographical hub for managing sports infrastructure within the system.
 * @author Matija Lukic
 * */
@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    /**
     * Unique identifier for the city.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the city.
     * Allowed values: Must not be null, empty, or blank. Maximum length is 100 characters.
     */
    @NotBlank(message = "City name cannot be blank")
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;

    /**
     * The postal or zip code associated with the city.
     * Allowed values: Must not be null, empty, or blank. Maximum length is 5 characters.
     */
    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 5, message = "Postal code cannot exceed 5 characters")
    @Column(name = "postal_code")
    private String postalCode;

    /**
     * The country in which the city is located.
     */
    private String country;

    /**
     * The list of tournaments that take place in this city.
     * Changes to the city will cascade to its associated tournaments.
     */
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Tournament> tournaments;

    /**
     * The list of sports teams based in this city.
     * Changes to the city will cascade to its associated teams.
     */
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Team> teams;
}

