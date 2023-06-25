package model;

import domain.Position;
import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@Builder
public class Player {

    private final Long id;
    private final String name;
    private final Position position;
    private final Timestamp createdAt;
    private final Long teamId;

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
