package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a sports team participating in the tournament system.
 * Manages core team profiles, historical win-loss statistics, and links
 * relationships to its home city, roster of players, and scheduled matches.
 * @author Matija Lukic
 */
@Entity
@Table(name = "team")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    /**
     * Unique identifier for the sports team.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The official name of the sports team.
     * This field is mandatory and cannot be null.
     */
    @NotBlank(message = "Team name cannot be blank")
    @Size(max = 100, message = "Team name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;

    /**
     * The formal founding or registration date of the club.
     */
    @NotNull(message = "Foundation date is required")
    @Column(name = "foundation_date")
    private LocalDate foundationDate;

    /**
     * Total number of official match victories accumulated by the team.
     */
    @Min(value = 0, message = "Wins count cannot be negative")
    @Column(name = "wins_count")
    private int winsCount;

    /**
     * Total number of official match defeats accumulated by the team.
     */
    @Min(value = 0, message = "Lose count cannot be negative")
    @Column(name = "losses_count")
    private int lossesCount;

    /**
     * The home city where the sports club is originally based.
     * This field is mandatory and lazy-loaded.
     */
    @NotNull(message = "Team must be associated with a city")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    /**
     * The current list of athletes registered to the team's roster.
     * Structural changes to the team will cascade directly down to its players.
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Player> players;

    /**
     * The collection of tournament matches where this club competed as the home team.
     */
    @OneToMany(mappedBy = "homeTeam")
    private List<Match> homeMatches;

    /**
     * The collection of tournament matches where this club competed as the visiting/away team.
     */
    @OneToMany(mappedBy = "awayTeam")
    private List<Match> awayMatches;
}

