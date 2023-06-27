package model;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Stadium {

    private Long id;
    private String name;
    Timestamp createdAt;

    public static Stadium from(ResultSet resultSet) throws SQLException {
        return Stadium.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .createdAt(resultSet.getTimestamp("created_at"))
                .build();
    }
}
