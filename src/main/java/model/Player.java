package model;

import domain.Position;
import lombok.*;
import util.EnumParser;

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

        Position position = EnumParser.fromValue(Position.class, resultSet.getString("position"));

        return Player.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .position(position)
                .createdAt(resultSet.getTimestamp("created_at"))
                .teamId(resultSet.getLong("team_id"))
                .build();
    }

}
