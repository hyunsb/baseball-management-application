package dao;

import model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamDAO {

    private final Connection connection;

    public TeamDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Team 생성
     *
     * @param name
     * @return Optional Team
     * @throws SQLException
     */
    public Optional<Team> createTeam(String name, Long stadiumId) throws SQLException {
        String query = "INSERT INTO team (name, stadium_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setLong(2, stadiumId);

            int rowCount = statement.executeUpdate();
            if (rowCount > 0) return findTeamByName(name);
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
    public Optional<Team> findTeamByName(String name) throws SQLException {
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
     * @return List Team
     * @throws SQLException
     */
    public List<Team> findAll() throws SQLException {
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
     * Team 이름으로 삭제하기
     *
     * @param name
     * @throws SQLException
     */
    public void deleteByName(String name) throws SQLException {
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
    public void deleteAll() throws SQLException {
        String query = "DELETE FROM team";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }
}