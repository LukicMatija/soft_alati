package rs.fon.bg.ac.rs.lukic_matija.dtos.teamDtos;



import rs.fon.bg.ac.rs.lukic_matija.domain.Team;

import java.time.LocalDate;

public record TeamAddDto(String name, LocalDate foundationDate, Long cityId) {
    public Team toEntity(){
        return Team.builder().name(name).foundationDate(foundationDate).winsCount(0).lossesCount(0).build();
    }
}
