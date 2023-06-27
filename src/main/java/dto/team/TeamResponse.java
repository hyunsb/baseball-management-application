package dto.team;

import lombok.*;
import model.Team;

import java.sql.Timestamp;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class TeamResponse {

    private Long id;
    private String name;
    private Long stadiumId;
    private Timestamp createAt;

    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .stadiumId(team.getStadiumId())
                .createAt(team.getCreatedAt())
                .build();
    }
}
