package service;

import core.ConnectionPoolManager;
import dao.StadiumDAO;
import dao.TeamDAO;
import dto.team.TeamRequest;
import dto.team.TeamResponse;
import dto.team.TeamWithStadiumResponse;
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

    public TeamResponse save(TeamRequest request) throws TeamRegistrationFailureException, SQLException {
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

        return TeamResponse.from(result.orElseThrow(() ->
                new TeamRegistrationFailureException("팀이 존재하지 않습니다.")));
    }

    private void validateStadiumId(Long stadiumId) throws SQLException, TeamRegistrationFailureException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            if (stadiumDAO.findStadiumById(connection, stadiumId).isEmpty()) {
                throw new TeamRegistrationFailureException(stadiumId + "야구장ID가 참조하는 야구장이 존재하지 않습니다.");
            }
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<TeamWithStadiumResponse> findAllWithStadium() throws TeamFindFailureException, SQLException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<TeamWithStadium> result = teamDAO.findAllJoinStadium(connection);
            return result.stream()
                    .map(TeamWithStadiumResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new TeamFindFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    public List<TeamResponse> findAll() throws TeamFindFailureException, SQLException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<Team> result = teamDAO.findAll(connection);
            return result.stream()
                    .map(TeamResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new TeamFindFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }
}