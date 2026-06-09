package rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos;

import jakarta.validation.constraints.Min;

public record MatchUpdateDto(
        @Min(value = 0, message = "Home team points cant be negative")
        int homePoints,
        @Min(value = 0, message = "Away team points cant be negative")
        int awayPoints) {
}
