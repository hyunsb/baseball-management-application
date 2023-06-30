package service;

import dao.PlayerDAO;
import dto.player.PlayerDTO;
import lombok.RequiredArgsConstructor;
import model.Player;
import exception.FindPlayersFailureException;
import exception.PlayerRegistrationFailureException;
import exception.PlayerUpdateFailureException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerService {

    private final PlayerDAO playerDAO;

    public PlayerDTO.FindPlayerResponse save(PlayerDTO.NewPlayerRequest playerRequest) throws PlayerRegistrationFailureException {

        try {
            Long id = playerDAO.registerPlayer(playerRequest.getTeamId(), playerRequest.getName(), playerRequest.getPosition());
            return PlayerDTO.FindPlayerResponse.from(playerDAO.findById(id).orElseThrow(() -> new FindPlayersFailureException("해당 선수를 찾을 수 없습니다")));
        } catch (SQLException exception) {
            throw new PlayerRegistrationFailureException("선수 등록에 실패 하였습니다.");
        }
    }

    public void update(PlayerDTO.UpdatePlayerTeamIdForOutRequest updatePlayerTeamIdForOutRequest) throws PlayerUpdateFailureException {

        try {
            playerDAO.updatePlayer(updatePlayerTeamIdForOutRequest.getId());
        } catch (SQLException exception) {
            throw new PlayerUpdateFailureException("선수 정보 변경을 실패 하였습니다.");
        }
    }

    public List<PlayerDTO.FindPlayerResponse> findByTeam(PlayerDTO.FindPlayersByTeamRequest request) throws FindPlayersFailureException {

        try {
            List<Player> players = playerDAO.findByTeamId(request.getTeamId());
            return players.stream()
                    .map(PlayerDTO.FindPlayerResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("선수 조회에 실패 하였습니다.");
        }
    }

    public List<PlayerDTO.FindPlayerGroupByPositionResponse> findPlayerGroupByPosition() throws FindPlayersFailureException {

        try {
            List<Player> players = playerDAO.findPlayerGroupByPosition();
            return players.stream()
                    .map(PlayerDTO.FindPlayerGroupByPositionResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("선수 조회에 실패 하였습니다.");
        }
    }

}
