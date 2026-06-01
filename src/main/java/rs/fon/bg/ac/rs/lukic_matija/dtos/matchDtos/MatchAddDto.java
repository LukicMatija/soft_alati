package rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Match;
import rs.fon.bg.ac.rs.lukic_matija.domain.MatchPhase;

import java.time.LocalDateTime;

public record MatchAddDto(LocalDateTime scheduleTime,boolean played, String phase, Long tournamentId, Long homeTeamId, Long awayTeamId, int homePoints, int awayPoints) {
    public Match toEntity(){
        return Match.builder().scheduleTime(scheduleTime).played(played).phase(MatchPhase.valueOf(phase)).homePoints(homePoints).awayPoints(awayPoints).build();
    }
}
