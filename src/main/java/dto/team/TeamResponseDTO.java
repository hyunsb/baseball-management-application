package dto.team;

import lombok.*;
import model.Team;

import java.sql.Timestamp;

@Getter
@Builder
public class TeamResponseDTO {

    private Long id;
    private String name;
    private Long stadiumId;
    private Timestamp createAt;

    public static TeamResponseDTO from(Team team) {
        return TeamResponseDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .stadiumId(team.getStadiumId())
                .createAt(team.getCreatedAt())
                .build();
    }

    @Override
    public String toString() {
        return "ID:" + id +
                ", NAME: " + name +
                ", STADIUM_ID: " + stadiumId +
                ", 생성일자: " + createAt;
    }
}
