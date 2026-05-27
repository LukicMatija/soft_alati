package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "referee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String license;

    @Column(name = "experience_level")
    private int experienceLevel;

    @OneToMany(mappedBy = "referee", cascade = CascadeType.ALL)
    private List<Delegation> delegations;
}

