package model;

import domain.Position;
import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Player {

    private Long id;
    private String name;
    private Position position;
    Timestamp createdAt;
    private Long teamId;

    public static Player from(ResultSet resultSet) throws SQLException {
        Position position = getPosition(resultSet.getString("position"));
        return Player.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .position(position)
                .createdAt(resultSet.getTimestamp("created_at"))
                .teamId(resultSet.getLong("teamId"))
                .build();
    }

    private static Position getPosition(String positionName) {
        return Position.valueOf(positionName);
    }
}
