package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
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
     * This field is mandatory and cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The postal or zip code associated with the city.
     */
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

