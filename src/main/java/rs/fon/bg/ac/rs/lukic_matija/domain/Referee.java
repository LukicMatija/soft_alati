package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Represents a sports official or referee certified within the system.
 * Stores personal identity credentials, officiating license classification,
 * professional experience levels, and tracks historical match assignment delegations.
 * @author Matija Lukic
 */
@Entity
@Table(name = "referee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referee {

    /**
     * Unique identifier for the referee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full name (first name and last name) of the referee.
     * This field is mandatory and cannot be null.
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * The type or class of official license held by the referee (e.g., FIFA Badge, National License).
     */
    private String license;

    /**
     * The experience level of the referee, typically represented in years or tier rankings.
     */
    @Column(name = "experience_level")
    private int experienceLevel;

    /**
     * The list of match delegations to which this referee has been assigned.
     * Any cascading changes on the referee entity will reflect across their associated delegations.
     */
    @OneToMany(mappedBy = "referee", cascade = CascadeType.ALL)
    private List<Delegation> delegations;
}

