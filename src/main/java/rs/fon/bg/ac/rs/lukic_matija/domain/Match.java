package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "match_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;

    private boolean played;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchPhase phase;

    @Column(name = "home_points")
    private int homePoints;

    @Column(name = "away_points")
    private int awayPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<Delegation> delegations;
}

