package rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Delegation;

public record DelegationResponseDto(String role, double score, String refereeName,String tourName, String homeTean, String awayTeam) {
    public static DelegationResponseDto fromEntity(Delegation d){
        String refereeName = (d.getReferee().getFullName()!=null) ? d.getReferee().getFullName() : "Nema ime sudije";
        String homeTeam = (d.getMatch().getHomeTeam().getName()!=null) ? d.getMatch().getHomeTeam().getName() : "Nema domaceg tima";
        String awayTeam = (d.getMatch().getAwayTeam().getName()!=null) ? d.getMatch().getAwayTeam().getName() : "Nema gostujuceg tima";
        String tourName = (d.getMatch().getTournament().getName()!=null) ? d.getMatch().getTournament().getName() : "Nema turnira";
        return new DelegationResponseDto(d.getRefereeRole(),d.getOfficiatingScore(),refereeName,tourName,homeTeam,awayTeam);
    }
}
