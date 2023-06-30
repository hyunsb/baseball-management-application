package service;

import exception.FindPlayersFailureException;
import exception.PlayerRegistrationFailureException;
import dao.OutPlayerDAO;
import dao.PlayerDAO;
import dto.player.OutPlayerDTO;
import lombok.RequiredArgsConstructor;
import model.OutPlayer;
import exception.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OutPlayerService {

    private final OutPlayerDAO outPlayerDAO;
    private final PlayerDAO playerDAO;
    private final Connection connection;

    public OutPlayerDTO.FindOutPlayerResponse save(OutPlayerDTO.NewOutPlayerRequest newOutPlayerRequest) throws RollbackException, PlayerRegistrationFailureException {

        try {
            connection.setAutoCommit(false);

            outPlayerDAO.registerOutPlayer(newOutPlayerRequest.getReason(), newOutPlayerRequest.getPlayerId());
            playerDAO.updatePlayer(newOutPlayerRequest.getPlayerId());

            connection.commit();
            return OutPlayerDTO.FindOutPlayerResponse.from(outPlayerDAO.findById(newOutPlayerRequest.getPlayerId()).orElseThrow(() -> new FindPlayersFailureException("Failed to Find Out user id: " + newOutPlayerRequest.getPlayerId())));
        } catch (SQLException | FindPlayersFailureException | PlayerUpdateFailureException exception) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RollbackException(rollbackException.getMessage());
            }
            throw new PlayerRegistrationFailureException("퇴출 선수 등록에 실패하였습니다");
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }



    public List<OutPlayerDTO.FindOutPlayerResponse> findOutPlayers() throws FindPlayersFailureException {
        try {
            List<OutPlayer> outPlayers = outPlayerDAO.findOutPlayers();
            return outPlayers.stream()
                    .map(OutPlayerDTO.FindOutPlayerResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("선수 등록에 실패 하였습니다.");
        }
    }

}
