package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sponsor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String company;

    @Column(name = "contract_budget")
    private double contractBudget;

    @Column(name = "signing_date")
    private LocalDate signingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
}

