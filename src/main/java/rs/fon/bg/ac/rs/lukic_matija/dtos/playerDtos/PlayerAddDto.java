package rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Player;

public record PlayerAddDto(Long teamId, String firstName, String lastName, String position, int jerseyNumber, boolean injured) {
    public Player toEntity(){
        return Player.builder().firstName(firstName).lastName(lastName).position(position).jerseyNumber(jerseyNumber).injured(injured).build();
    }
}
