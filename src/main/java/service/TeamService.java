package service;

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

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TeamService {

    private final TeamDAO teamDAO;
    private final StadiumDAO stadiumDAO;

    public TeamResponse save(TeamRequest request) throws TeamRegistrationFailureException {
        Optional<Team> result;
        String name = request.getName();
        Long stadiumId = request.getStadiumId();

        try {
            validateStadiumId(stadiumId);
            result = teamDAO.createTeam(name, stadiumId);

        } catch (SQLException exception) {
            throw new TeamRegistrationFailureException(exception.getMessage());
        }

        return TeamResponse.from(result.orElseThrow(() ->
                new TeamRegistrationFailureException("팀이 존재하지 않습니다.")));
    }

    private void validateStadiumId(Long stadiumId) throws SQLException, TeamRegistrationFailureException {
        if (stadiumDAO.findStadiumById(stadiumId).isEmpty())
            throw new TeamRegistrationFailureException(stadiumId + "야구장ID가 참조하는 야구장이 존재하지 않습니다.");
    }

    public List<TeamWithStadiumResponse> findAllWithStadium() throws TeamFindFailureException {
        try {
            List<TeamWithStadium> result = teamDAO.findAllJoinStadium();
            return result.stream()
                    .map(TeamWithStadiumResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new TeamFindFailureException(exception.getMessage());
        }
    }

    public List<TeamResponse> findAll() throws TeamFindFailureException {
        try {
            List<Team> result = teamDAO.findAll();
            return result.stream()
                    .map(TeamResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new TeamFindFailureException(exception.getMessage());
        }
    }
}