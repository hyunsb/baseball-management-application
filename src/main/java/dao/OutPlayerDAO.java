package dao;

import exception.ErrorMessage;
import model.OutPlayer;
import exception.PlayerRegistrationFailureException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class OutPlayerDAO {

    /**
     * Register Player
     *
     * @param reason   퇴출 이유
     * @param playerId 선수 아이디
     * @throws SQLException
     */
    public void registerOutPlayer(Connection connection, String reason, Long playerId) throws SQLException {

        String query = "INSERT INTO out_player (reason, player_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, reason);
            preparedStatement.setLong(2, playerId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows < 0) {
                throw new PlayerRegistrationFailureException(ErrorMessage.FAILED_PLAYER_REGISTRATION_SQL);
            }

        }
    }

    /**
     * Find OutPlayer by player id
     *
     * @param id 선수 id
     * @return Matched Out Player by id as Optional, Optional.empty() if not found
     */
    public Optional<OutPlayer> findById(Connection connection, Long id) throws SQLException {

        String query = "SELECT p.id AS id, p.name AS name, p.position AS position, op.reason AS out_reason, op.created_at AS out_date\n" +
                "FROM out_player op\n" +
                "JOIN player p ON op.player_id = p.id\n" +
                "WHERE p.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(OutPlayer.from(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * find OutPlayers
     *
     * @return Matched List of Out Players
     * @throws SQLException
     */
    public List<OutPlayer> findOutPlayers(Connection connection) throws SQLException {

        String query = "SELECT\n" +
                "    p.id AS id,\n" +
                "    p.name AS name,\n" +
                "    p.position AS position,\n" +
                "    op.reason AS out_reason,\n" +
                "    op.created_at AS out_date\n" +
                "FROM\n" +
                "    player p\n" +
                "LEFT JOIN\n" +
                "    out_player op ON p.id = op.player_id;\n";

        List<OutPlayer> players = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                players.add(OutPlayer.from(resultSet));
            }
        }

        return players;
    }
}
