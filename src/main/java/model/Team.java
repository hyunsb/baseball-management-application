package model;

import lombok.Builder;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@Builder
public class Team {

    private Long id;
    private String name;
    private Long stadiumId;
    private Timestamp createdAt;

    public static Team from(ResultSet resultSet) throws SQLException {
        return Team.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .stadiumId(resultSet.getLong("stadium_id"))
                .createdAt(resultSet.getTimestamp("created_at"))
                .build();
    }
}
