package dao;

import model.Team;
import model.TeamWithStadium;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamDAO {

    /**
     * Team 생성
     *
     * @param name
     * @return Optional Team
     * @throws SQLException
     */
    public Optional<Team> createTeam(Connection connection, String name, Long stadiumId) throws SQLException {
        String query = "INSERT INTO team (name, stadium_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setLong(2, stadiumId);

            int rowCount = statement.executeUpdate();
            if (rowCount > 0) return findTeamByName(connection, name);
        }
        return Optional.empty(); // error
    }

    /**
     * Team 이름으로 검색
     *
     * @param name
     * @return Optional Team
     * @throws SQLException
     */
    public Optional<Team> findTeamByName(Connection connection, String name) throws SQLException {
        String query = "SELECT * FROM team WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Team.from(resultSet));
                }
            }
        }
        return Optional.empty(); // not found
    }

    /**
     * Team 전체 목록 검색
     *
     * @return List Team
     * @throws SQLException
     */
    public List<Team> findAll(Connection connection) throws SQLException {
        String query = "SELECT * FROM team";

        List<Team> teams = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    teams.add(Team.from(resultSet));
                }
            }
        }
        return teams;
    }

    /**
     * Team 전체 목록과 Stadium 정보를 검색
     *
     * @return List TeamWithStadium
     * @throws SQLException
     */
    public List<TeamWithStadium> findAllJoinStadium(Connection connection) throws SQLException {
        String query = "SELECT t.id AS team_id, t.name AS team_name, t.created_at AS team_date, " +
                "s.id AS stadium_id, s.name AS stadium_name, s.created_at AS stadium_date\n" +
                "FROM team AS t\n" +
                "JOIN stadium AS s ON t.stadium_id = s.id\n";

        List<TeamWithStadium> teamWithStadiums = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    teamWithStadiums.add(TeamWithStadium.from(resultSet));
                }
            }
        }
        return teamWithStadiums;
    }

    /**
     * Team 이름으로 삭제하기
     *
     * @param name
     * @throws SQLException
     */
    public void deleteByName(Connection connection, String name) throws SQLException {
        String query = "DELETE FROM team WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    /**
     * Team 전체 삭제하기
     *
     * @throws SQLException
     */
    public void deleteAll(Connection connection) throws SQLException {
        String query = "DELETE FROM team";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }
}