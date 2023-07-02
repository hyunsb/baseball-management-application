package service;

import core.ConnectionPoolManager;
import dao.PlayerDAO;
import dto.player.PlayerDTO;
import lombok.RequiredArgsConstructor;
import model.Player;
import exception.FindPlayersFailureException;
import exception.PlayerRegistrationFailureException;
import exception.PlayerUpdateFailureException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerService {

    private final ConnectionPoolManager connectionPoolManager;
    private final PlayerDAO playerDAO;

    public PlayerDTO.FindPlayerResponse save(PlayerDTO.NewPlayerRequest playerRequest) throws PlayerRegistrationFailureException, SQLException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            Long id = playerDAO.registerPlayer(connection, playerRequest.getTeamId(), playerRequest.getName(), playerRequest.getPosition());
            return PlayerDTO.FindPlayerResponse.from(playerDAO.findById(connection, id).orElseThrow(() -> new FindPlayersFailureException("해당 선수를 찾을 수 없습니다")));
        } catch (SQLException exception) {
            throw new PlayerRegistrationFailureException("선수 등록에 실패 하였습니다.");
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public void update(PlayerDTO.UpdatePlayerTeamIdForOutRequest updatePlayerTeamIdForOutRequest) throws PlayerUpdateFailureException, SQLException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            playerDAO.updatePlayer(connection, updatePlayerTeamIdForOutRequest.getId());
        } catch (SQLException exception) {
            throw new PlayerUpdateFailureException("선수 정보 변경을 실패 하였습니다.");
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<PlayerDTO.FindPlayerResponse> findByTeam(PlayerDTO.FindPlayersByTeamRequest request) throws FindPlayersFailureException, SQLException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<Player> players = playerDAO.findByTeamId(connection, request.getTeamId());
            return players.stream()
                    .map(PlayerDTO.FindPlayerResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("선수 조회에 실패 하였습니다.");
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<PlayerDTO.FindPlayerGroupByPositionResponse> findPlayerGroupByPosition() throws FindPlayersFailureException, SQLException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<Player> players = playerDAO.findPlayerGroupByPosition(connection);
            return players.stream()
                    .map(PlayerDTO.FindPlayerGroupByPositionResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("선수 조회에 실패 하였습니다.");
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

}
