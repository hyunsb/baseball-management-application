package dao;

import model.Stadium;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StadiumDAO {

    /**
     * Stadium 생성
     *
     * @param name
     * @return Optional Stadium
     * @throws SQLException
     */
    public Optional<Stadium> createStadium(Connection connection, String name) throws SQLException {
        String query = "INSERT INTO stadium (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);

            int rowCount = statement.executeUpdate();
            if (rowCount > 0) return findStadiumByName(connection, name);
        }
        return Optional.empty(); // error
    }

    /**
     * Stadium id로 검색
     *
     * @param id
     * @return Optional Stadium
     * @throws SQLException
     */
    public Optional<Stadium> findStadiumById(Connection connection, Long id) throws SQLException {
        String query = "SELECT * FROM stadium WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Stadium.from(resultSet));
                }
            }
        }
        return Optional.empty(); // not found
    }

    /**
     * Stadium 이름으로 검색
     *
     * @param name
     * @return Optional Stadium
     * @throws SQLException
     */
    public Optional<Stadium> findStadiumByName(Connection connection, String name) throws SQLException {
        String query = "SELECT * FROM stadium WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Stadium.from(resultSet));
                }
            }
        }
        return Optional.empty(); // not found
    }

    /**
     * Stadium 전체 목록 검색
     *
     * @return
     * @throws SQLException
     */
    public List<Stadium> findAll(Connection connection) throws SQLException {
        String query = "SELECT * FROM stadium ORDER BY id asc";

        List<Stadium> stadiums = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stadiums.add(Stadium.from(resultSet));
                }
            }
        }
        return stadiums;
    }

    /**
     * 이름으로 야구장 삭제하기
     *
     * @param name
     * @throws SQLException
     */
    public void deleteByName(Connection connection, String name) throws SQLException {
        String query = "DELETE FROM stadium WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    /**
     * 전체 야구장 삭제하기
     *
     * @throws SQLException
     */
    public void deleteAll(Connection connection) throws SQLException {
        String query = "DELETE FROM stadium";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }
}
