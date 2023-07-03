package service;

import core.ConnectionPoolManager;
import dao.StadiumDAO;
import dao.TeamDAO;
import dto.team.TeamRequestDTO;
import dto.team.TeamResponseDTO;
import dto.team.TeamWithStadiumResponseDTO;
import exception.ErrorMessage;
import exception.TeamFindFailureException;
import exception.TeamRegistrationFailureException;
import lombok.RequiredArgsConstructor;
import model.Team;
import model.TeamWithStadium;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TeamService {

    private final ConnectionPoolManager connectionPoolManager;
    private final TeamDAO teamDAO;
    private final StadiumDAO stadiumDAO;

    public TeamResponseDTO save(TeamRequestDTO request) throws TeamRegistrationFailureException {
        Connection connection = connectionPoolManager.getConnection();
        Optional<Team> result;
        String name = request.getName();
        Long stadiumId = request.getStadiumId();

        try {
            validateStadiumId(stadiumId);
            result = teamDAO.createTeam(connection, name, stadiumId);

        } catch (SQLException exception) {
            throw new TeamRegistrationFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }

        return TeamResponseDTO.from(result.orElseThrow(() ->
                new TeamRegistrationFailureException(ErrorMessage.FAILED_TEAM_FIND)));
    }

    private void validateStadiumId(Long stadiumId) throws SQLException, TeamRegistrationFailureException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            if (stadiumDAO.findStadiumById(connection, stadiumId).isEmpty()) {
                throw new TeamRegistrationFailureException(ErrorMessage.FAILED_TEAM_FIND);
            }
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<TeamWithStadiumResponseDTO> findAllWithStadium() throws TeamFindFailureException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<TeamWithStadium> result = teamDAO.findAllJoinStadium(connection);
            return result.stream()
                    .map(TeamWithStadiumResponseDTO::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new TeamFindFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<TeamResponseDTO> findAll() throws TeamFindFailureException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<Team> result = teamDAO.findAll(connection);
            return result.stream()
                    .map(TeamResponseDTO::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new TeamFindFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }
}