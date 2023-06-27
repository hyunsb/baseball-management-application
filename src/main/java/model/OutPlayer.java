package model;

import domain.Position;
import domain.Reason;
import lombok.Builder;
import lombok.Getter;
import util.EnumParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@Builder
public class OutPlayer {

    private final Long playerId;
    private final String name;
    private final Position position;
    private final Reason reason;
    private final Timestamp outDay;

    public static OutPlayer from(ResultSet resultSet) throws SQLException {

        Reason reason = EnumParser.fromValue(Reason.class, resultSet.getString("out_reason"));
        Position position = EnumParser.fromValue(Position.class, resultSet.getString("position"));

        return OutPlayer.builder()
                .playerId(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .position(position)
                .reason(reason)
                .outDay(resultSet.getTimestamp("out_date"))
                .build();
    }
}
