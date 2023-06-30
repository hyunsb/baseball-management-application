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

    public PlayerDTO.FindPlayerResponse save(PlayerDTO.NewPlayerRequest playerRequest) {

        try {
            Long id = playerDAO.registerPlayer(playerRequest.getTeamId(), playerRequest.getName(), playerRequest.getPosition());
            return PlayerDTO.FindPlayerResponse.from(playerDAO.findById(id).orElseThrow(() -> new FindPlayersFailureException("Failed to Find user id:" + id)));
        } catch (SQLException exception) {
            throw new PlayerRegistrationFailureException("Failed to register player while executing SQL.\nCause: " + exception.getMessage());
        }
    }

    public void update(PlayerDTO.UpdatePlayerTeamIdForOutRequest updatePlayerTeamIdForOutRequest){

        try {
            playerDAO.updatePlayer(updatePlayerTeamIdForOutRequest.getId());
        } catch (SQLException exception) {
            throw new PlayerUpdateFailureException("Failed to Update player while executing SQL.\nCause: " + exception.getMessage());
        }
    }

    public List<PlayerDTO.FindPlayerResponse> findByTeam(PlayerDTO.FindPlayersByTeamRequest request) {

        try {
            List<Player> players = playerDAO.findByTeamId(request.getTeamId());
            return players.stream()
                    .map(PlayerDTO.FindPlayerResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("Failed to Find players By Team While execute sql\nCause: " + exception.getMessage());
        }
    }

    public List<PlayerDTO.FindPlayerGroupByPositionResponse> findPlayerGroupByPosition() {

        try {
            List<Player> players = playerDAO.findPlayerGroupByPosition();
            return players.stream()
                    .map(PlayerDTO.FindPlayerGroupByPositionResponse::from)
                    .collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new FindPlayersFailureException("Failed to Find players Group By Position While execute sql\nCause: " + exception.getMessage());
        }
    }

}
