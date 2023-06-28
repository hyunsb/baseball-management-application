package dto.team;

import lombok.Builder;
import lombok.Getter;
import model.TeamWithStadium;

import java.sql.Timestamp;

@Getter
@Builder
public class TeamWithStadiumResponse {

    private Long teamId;
    private String teamName;
    private Timestamp teamCreatedAt;

    private Long stadiumId;
    private String stadiumName;
    private Timestamp stadiumCreatedAt;


    public static TeamWithStadiumResponse from(TeamWithStadium teamWithStadium) {
        return TeamWithStadiumResponse.builder()
                .teamId(teamWithStadium.getTeamId())
                .teamName(teamWithStadium.getTeamName())
                .teamCreatedAt(teamWithStadium.getTeamCreatedAt())
                .stadiumId(teamWithStadium.getStadiumId())
                .stadiumName(teamWithStadium.getStadiumName())
                .stadiumCreatedAt(teamWithStadium.getStadiumCreatedAt())
                .build();
    }

    @Override
    public String toString() {
        return "TEAM_ID: " + teamId +
                ", TEAM_NAME: " + teamName +
                ", 생성일자: " + teamCreatedAt +
                " -> 야구장 정보: [ STADIUM_ID: " + stadiumId +
                ", STADIUM_NAME: " + stadiumName +
                ", 생성일자: " + stadiumCreatedAt + " ]";
    }
}
