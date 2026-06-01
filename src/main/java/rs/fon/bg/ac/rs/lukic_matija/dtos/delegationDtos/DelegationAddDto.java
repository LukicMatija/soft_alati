package rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Delegation;

public record DelegationAddDto(String role, double score, Long matchId, Long refereeId) {
    public Delegation toEntity(){
        return Delegation.builder().refereeRole(role).officiatingScore(score).build();
    }
}
