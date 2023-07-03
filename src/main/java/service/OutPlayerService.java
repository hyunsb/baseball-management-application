package service;

import core.ConnectionPoolManager;
import dao.OutPlayerDAO;
import dao.PlayerDAO;
import dto.player.OutPlayerDTO;
import exception.*;
import lombok.RequiredArgsConstructor;
import model.OutPlayer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OutPlayerService {

    private final ConnectionPoolManager connectionPoolManager;
    private final OutPlayerDAO outPlayerDAO;
    private final PlayerDAO playerDAO;

    public OutPlayerDTO.FindOutPlayerResponse save(OutPlayerDTO.NewOutPlayerRequest newOutPlayerRequest)
            throws RollbackException, PlayerRegistrationFailureException {

        Connection connection = connectionPoolManager.getConnection();
        try {
            connection.setAutoCommit(false);

            outPlayerDAO.registerOutPlayer(connection, newOutPlayerRequest.getReason(), newOutPlayerRequest.getPlayerId());
            playerDAO.updatePlayer(connection, newOutPlayerRequest.getPlayerId());

            connection.commit();
            return OutPlayerDTO.FindOutPlayerResponse.from(outPlayerDAO.findById(connection, newOutPlayerRequest.getPlayerId()).orElseThrow(() -> new FindPlayersFailureException("Failed to Find Out user id: " + newOutPlayerRequest.getPlayerId())));
        } catch (SQLException | FindPlayersFailureException | PlayerUpdateFailureException exception) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RollbackException(rollbackException.getMessage());
            }
            throw new PlayerRegistrationFailureException(ErrorMessage.FAILED_OUT_PLAYER_REGISTRATION);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<OutPlayerDTO.FindOutPlayerResponse> findOutPlayers() throws FindPlayersFailureException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<OutPlayer> outPlayers = outPlayerDAO.findOutPlayers(connection);
            return outPlayers.stream()
                    .map(OutPlayerDTO.FindOutPlayerResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException(ErrorMessage.FAILED_OUT_PLAYER_REGISTRATION);
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }
}
