package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
     * Allowed values: Must not be null, empty, or blank. Maximum length is 100 characters.
     */
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * The type or class of official license held by the referee (e.g., FIFA Badge, National License).
     */
    private String license;

    /**
     * The experience level of the referee, typically represented in years or tier rankings.
     * Allowed values: Must be a non-negative integer value (0 or greater).
     */
    @Min(value = 0, message = "Experience level cannot be negative")
    @Column(name = "experience_level")
    private int experienceLevel;

    /**
     * The list of match delegations to which this referee has been assigned.
     * Any cascading changes on the referee entity will reflect across their associated delegations.
     */
    @OneToMany(mappedBy = "referee", cascade = CascadeType.ALL)
    private List<Delegation> delegations;
}

