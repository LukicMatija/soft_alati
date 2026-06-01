package rs.fon.bg.ac.rs.lukic_matija.dtos.matchDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Match;

import java.time.LocalDateTime;

public record MatchResponseDto (LocalDateTime scheduleTime,boolean played, String phase, String tournamentName,
                                String homeTeamName, String awayTeamName, int homePoints, int awayPoints,
                                String result){

    public static MatchResponseDto fromEntity(Match match){
        String tourName = (match.getTournament() != null) ? match.getTournament().getName() : "Nema turnira";
        String homeTeamName = (match.getHomeTeam() != null) ? match.getHomeTeam().getName() : "Nema domaceg tima";
        String awayTeamName = (match.getAwayTeam() != null) ? match.getAwayTeam().getName() : "Nema gostujuceg tima";
        String result;
        if(match.isPlayed()){
            int comp = Integer.compare(match.getHomePoints(), match.getAwayPoints());
            result = switch (comp) {
                case 1 -> "Pobednik je domaci tim";
                case -1 -> "Pobednik je gostujuci tim";
                case 0 -> "Mec je završen nereseno!";
                default -> "Nepoznat ishod.";
            };
        }
        else {
            result = "Mec nije odigran";
        }
        return new MatchResponseDto(match.getScheduleTime(),match.isPlayed(), match.getPhase().toString()
                ,tourName,homeTeamName,awayTeamName,match.getHomePoints(),match.getAwayPoints(),result);
    }
}
