package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


/**
 * Represents an individual athlete or player registered within the system.
 * Contains personal detail attributes, field positions, jersey numbers,
 * health/injury status, and maps the relationship to their current sports team.
 * @author Matija Lukic
 */
@Entity
@Table(name = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    /**
     * Unique identifier for the player.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the player.
     * Allowed values: Must not be null, empty, or blank. Maximum length is 50 characters.
     */
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Column(nullable = false)
    private String firstName;

    /**
     * The last name of the player.
     * Allowed values: Must not be null, empty, or blank. Maximum length is 50 characters.
     */
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Column(nullable = false)
    private String lastName;

    /**
     * The playing position of the player on the field (e.g., Striker, Midfielder, Defender).
     */
    private String position;

    /**
     * The squad or jersey number assigned to the player within their team.
     * Allowed values: Must be an integer value between 1 and 99 inclusive.
     */
    @Min(value = 1, message = "Jersey number must be at least 1")
    @Max(value = 99, message = "Jersey number cannot be greater than 99")
    @Column(name = "jersey_number")
    private int jerseyNumber;

    /**
     * Indicates whether the player is currently injured and unavailable for matches.
     * Allowed values: True if injured, false if healthy.
     */
    private boolean injured;

    /**
     * The sports team that the player currently belongs to.
     * This field is mandatory and lazy-loaded.
     */
    @NotNull(message = "Player must belong to a team")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}

