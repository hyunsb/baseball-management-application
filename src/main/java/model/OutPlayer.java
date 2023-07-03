package model;

import domain.Position;
import domain.Reason;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
    @Setter
    private Reason reason;
    @Setter
    private Timestamp outDay;

    public static OutPlayer from(ResultSet resultSet) throws SQLException {
        Position position = EnumParser.fromValue(Position.class, resultSet.getString("position"));

        OutPlayerBuilder outPlayerBuilder =  OutPlayer.builder()
                .playerId(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .position(position);


        try {
            Reason reason = EnumParser.fromValue(Reason.class, resultSet.getString("out_reason"));
            outPlayerBuilder
                    .reason(reason)
                    .outDay(resultSet.getTimestamp("out_date"));

        } catch (Exception exception) {

        }
        return outPlayerBuilder.build();

    }
}
