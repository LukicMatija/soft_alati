package rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos;

import jakarta.validation.constraints.DecimalMin;
import rs.fon.bg.ac.rs.lukic_matija.domain.Sponsor;

import java.time.LocalDate;

public record SponsorAddDto(String company, @DecimalMin(value = "0.01", message = "Budget cant be 0 or lower") double budget, LocalDate signingDate, Long tournamentId) {
    public Sponsor toEntity(){
        return Sponsor.builder().company(company).contractBudget(budget).signingDate(signingDate).build();
    }
}
