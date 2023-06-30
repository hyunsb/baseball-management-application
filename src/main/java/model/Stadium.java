package model;

import lombok.Builder;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@Builder
public class Stadium {

    private Long id;
    private String name;
    private Timestamp createdAt;

    public static Stadium from(ResultSet resultSet) throws SQLException {
        return Stadium.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .createdAt(resultSet.getTimestamp("created_at"))
                .build();
    }
}
