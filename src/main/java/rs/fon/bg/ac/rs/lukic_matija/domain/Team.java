package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String name;

    /**
     * The formal founding or registration date of the club.
     */
    @Column(name = "foundation_date")
    private LocalDate foundationDate;

    /**
     * Total number of official match victories accumulated by the team.
     */
    @Column(name = "wins_count")
    private int winsCount;

    /**
     * Total number of official match defeats accumulated by the team.
     */
    @Column(name = "losses_count")
    private int lossesCount;

    /**
     * The home city where the sports club is originally based.
     * This field is mandatory and lazy-loaded.
     */
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

