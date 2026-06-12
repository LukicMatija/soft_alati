package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Represents a single sports match within a tournament.
 * Encapsulates scheduling details, live scoring, tournament phase tracking,
 * and maps the participating teams along with official match delegations.
 * @author Matija Lukic
 */
@Entity
@Table(name = "match_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    /**
     * Unique identifier for the match.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date and time when the match is scheduled to take place.
     */
    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;

    /**
     * Indicates whether the match has been played and completed.
     */
    private boolean played;

    /**
     * The specific phase of the tournament this match belongs to (e.g., QUARTERFINALS, SEMIFINALS, FINALS).
     * This field is mandatory and stored as a string representation in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchPhase phase;

    /**
     * The total points or score accumulated by the home team.
     * This field can not be negative
     */
    @Min(value = 0, message = "Home team points cannot be negative")
    @Column(name = "home_points")
    private int homePoints;

    /**
     * The total points or score accumulated by the away team.
     * This field can not be negative
     */
    @Min(value = 0, message = "Away team points cannot be negative")
    @Column(name = "away_points")
    private int awayPoints;

    /**
     * The tournament under which this match is organized.
     * This field is mandatory and lazy-loaded.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    /**
     * The team designated as the host or home team for the match.
     * This field is mandatory and lazy-loaded.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    /**
     * The team designated as the visitor or away team for the match.
     * This field is mandatory and lazy-loaded.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    /**
     * The list of referee delegations assigned to officiate this match.
     * Any changes to the match will cascade to its associated delegations.
     */
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<Delegation> delegations;
}

