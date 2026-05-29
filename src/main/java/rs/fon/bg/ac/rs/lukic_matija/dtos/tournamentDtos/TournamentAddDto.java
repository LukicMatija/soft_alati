package rs.fon.bg.ac.rs.lukic_matija.dtos.tournamentDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Tournament;


public record TournamentAddDto(String name, double prizePool, Long cityId) {
    public Tournament toEntity(){
        return Tournament.builder().name(name).prizePool(prizePool).build();
    }
}
