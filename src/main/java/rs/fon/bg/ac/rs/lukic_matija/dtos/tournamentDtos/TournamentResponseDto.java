package rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;

public record TournamentResponseDto(String name, double prizePool, String cityName) {
    public static TournamentResponseDto fromEntity(Tournament tournament) {
        String cityName = (tournament.getCity() != null) ? tournament.getCity().getName() : "Nema grada";

        return new TournamentResponseDto(
                tournament.getName(),
                tournament.getPrizePool(),
                cityName
        );
    }
}
