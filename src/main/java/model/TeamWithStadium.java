package model;

import lombok.Builder;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@Builder
public class TeamWithStadium {

    private Long teamId;
    private String teamName;
    private Timestamp teamCreatedAt;

    private Long stadiumId;
    private String stadiumName;
    private Timestamp stadiumCreatedAt;

    public static TeamWithStadium from(ResultSet resultSet) throws SQLException {
        return TeamWithStadium.builder()
                .teamId(resultSet.getLong("team_id"))
                .teamName(resultSet.getString("team_name"))
                .teamCreatedAt(resultSet.getTimestamp("team_date"))
                .stadiumId(resultSet.getLong("stadium_id"))
                .stadiumName(resultSet.getString("stadium_name"))
                .stadiumCreatedAt(resultSet.getTimestamp("stadium_date"))
                .build();
    }
}
