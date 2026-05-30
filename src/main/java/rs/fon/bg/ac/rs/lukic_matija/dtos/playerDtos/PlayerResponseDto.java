package rs.fon.bg.ac.rs.lukic_matija.dtos.playerDtos;

import rs.fon.bg.ac.rs.lukic_matija.domain.Player;
import rs.fon.bg.ac.rs.lukic_matija.domain.Team;

public record PlayerResponseDto(String teamName, String firstName, String lastName, String position, int jerseyNumber, String injured) {
    public static PlayerResponseDto fromEntity(Player player){
        String teamName = (player.getTeam()!=null) ? player.getTeam().getName() : "Nema tima";
        return new PlayerResponseDto(
                teamName, player.getFirstName(), player.getLastName(), player.getPosition(), player.getJerseyNumber(), (!player.isInjured()) ? "Zdrav" : "Povredjen"
        );
    }
}
