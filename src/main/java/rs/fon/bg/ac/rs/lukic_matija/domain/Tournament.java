package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;


/**
 * Represents a sports tournament organized within the system.
 * Serves as the central entity aggregating the financial prize structure,
 * geographical hosting details, financial backing sponsors, and scheduled matches.
 * @author Matija Lukic
 */
@Entity
@Table(name = "tournament")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {

    /**
     * Unique identifier for the tournament.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The official name of the tournament event.
     * This field is mandatory and cannot be null.
     */
    @NotBlank(message = "Tournament name cannot be blank")
    @Size(max = 150, message = "Tournament name cannot exceed 150 characters")
    @Column(nullable = false)
    private String name;

    /**
     * The total financial prize pool distributed among winning teams.
     */
    @DecimalMin(value = "0.01", message = "Prize pool must be greater than 0")
    @Column(name = "prize_pool")
    private double prizePool;

    /**
     * The host city where the tournament events are physically located.
     * This field is mandatory and lazy-loaded.
     */
    @NotNull(message = "Tournament must be associated with a city")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    /**
     * The list of commercial sponsors financing this specific tournament.
     * Modifications to the tournament will cascade to its associated sponsors.
     */
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Sponsor> sponsors;

    /**
     * The schedule of matches played as part of this tournament framework.
     * Modifications to the tournament will cascade to its associated matches.
     */
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches;
}

