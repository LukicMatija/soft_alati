package rs.fon.bg.ac.rs.lukic_matija.dtos.sponsorDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Sponsor;

import java.time.LocalDate;

public record SponsorResponseDto(String company, double budget, LocalDate signingDate, String tourName) {
    public static SponsorResponseDto fromEntity(Sponsor s){
        String tourName = (s.getTournament().getName()!=null) ? s.getTournament().getName() : "Nema naziva turnira";
        return new SponsorResponseDto(s.getCompany(),s.getContractBudget(),s.getSigningDate(),tourName);
    }
}
