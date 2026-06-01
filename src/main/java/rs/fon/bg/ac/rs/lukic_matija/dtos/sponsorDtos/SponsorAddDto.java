package rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Sponsor;

import java.time.LocalDate;

public record SponsorAddDto(String company, double budget, LocalDate signingDate, Long tournamentId) {
    public Sponsor toEntity(){
        return Sponsor.builder().company(company).contractBudget(budget).signingDate(signingDate).build();
    }
}
