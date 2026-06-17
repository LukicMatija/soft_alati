package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * Represents the assignment of a referee to a specific match.
 * Tracks the performance and specific role of the referee during the game.
 * @author Matija Lukic
 */
@Entity
@Table(name = "delegation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delegation {

    /**
     * Unique identifier for the delegation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The specific role assigned to the referee for the match (e.g., Main Referee, Assistant Referee).
     * Allowed values: Must not be null, empty, or blank. Maximum length is 50 characters.
     */
    @NotBlank(message = "Referee role cannot be blank")
    @Size(max = 50, message = "Referee role cannot exceed 50 characters")
    @Column(name = "referee_role")
    private String refereeRole;

    /**
     * The performance score given to the referee based on their officiating in the match.
     * Allowed values: Must be a decimal value between 0.00 and 10.00 inclusive.
     */
    @DecimalMin(value = "0.00", message = "Officiating score cannot be negative")
    @DecimalMax(value = "10.00", message = "Officiating score cannot exceed 10.00")
    @Column(name = "officiating_score")
    private double officiatingScore;

    /**
     * The referee assigned to this specific match delegation.
     * This field is mandatory and cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id", nullable = false)
    private Referee referee;

    /**
     * The match to which the referee is being delegated.
     * This field is mandatory and cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
}

