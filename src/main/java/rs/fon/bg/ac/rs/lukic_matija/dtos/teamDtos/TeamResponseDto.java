package rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Team;
import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;
import rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos.TournamentResponseDto;

import java.time.LocalDate;

public record TeamResponseDto(String name, LocalDate foundationDate,String cityName) {
    public static TeamResponseDto fromEntity(Team team) {
        String cityName = (team.getCity() != null) ? team.getCity().getName() : "Nema grada";

        return new TeamResponseDto(
                team.getName(),
                team.getFoundationDate(),
                cityName
        );
    }
}
