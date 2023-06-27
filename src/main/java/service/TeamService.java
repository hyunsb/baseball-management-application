package service;

import dao.TeamDAO;
import dto.team.TeamRequest;
import dto.team.TeamResponse;
import lombok.RequiredArgsConstructor;
import model.Team;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TeamService {

    private final TeamDAO teamDAO;

    public TeamResponse save(TeamRequest.Create request) {
        Optional<Team> result;
        String name = request.getName();
        Long stadiumId = request.getStadiumId();

        try {
            result = teamDAO.createTeam(name, stadiumId);
        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [insert]: " + exception.getMessage());
        }
        Team team = result.orElseThrow(() -> new IllegalArgumentException("Team save 실패"));

        return TeamResponse.from(team);
    }

    public List<TeamResponse> findAll() {
        try {
            List<Team> result = teamDAO.findAll();
            return result.stream()
                    .map(TeamResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [find All]: " + exception.getMessage());
        }
    }

    // 전체 야구장 삭제
    public void deleteAll() {
        try {
            teamDAO.deleteAll();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [delete All]: " + exception.getMessage());
        }
    }
}