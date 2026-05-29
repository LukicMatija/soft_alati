package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Tournament> tournaments;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Team> teams;
}

