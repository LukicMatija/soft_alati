package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "tournament")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "prize_pool")
    private double prizePool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Sponsor> sponsors;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches;
}

