package rs.fon.bg.ac.rs.lukic_matija.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;


/**
 * Represents a commercial sponsor associated with a specific tournament.
 * Tracks the financing metadata including contract budgets, partnership signing dates,
 * and maps the organizational connection to the sponsored tournament.
 * @author Matija Lukic
 */
@Entity
@Table(name = "sponsor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sponsor {

    /**
     * Unique identifier for the sponsor contract.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the sponsoring enterprise or company.
     * This field is mandatory and cannot be null.
     */
    @NotBlank(message = "Company name cannot be blank")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    @Column(nullable = false)
    private String company;

    /**
     * The total financial budget allocated by the sponsor for the tournament contract.
     */
    @DecimalMin(value = "0.01", message = "Contract budget must be greater than 0")
    @Column(name = "contract_budget")
    private double contractBudget;

    /**
     * The specific date when the sponsorship agreement was officially signed.
     */
    @NotNull(message = "Signing date is required")
    @Column(name = "signing_date")
    private LocalDate signingDate;

    /**
     * The tournament backed by this sponsorship agreement.
     * This field is mandatory and lazy-loaded.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
}

