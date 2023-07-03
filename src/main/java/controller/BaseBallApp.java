package controller;

import core.annotation.RequestMapping;
import domain.Request;
import dto.player.OutPlayerDTO;
import dto.player.PlayerDTO;
import dto.stadium.StadiumRequestDTO;
import dto.stadium.StadiumResponseDTO;
import dto.team.TeamRequestDTO;
import dto.team.TeamResponseDTO;
import dto.team.TeamWithStadiumResponseDTO;
import exception.*;
import lombok.RequiredArgsConstructor;
import service.OutPlayerService;
import service.PlayerService;
import service.StadiumService;
import service.TeamService;
import view.View;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BaseBallApp {

    private final StadiumService stadiumService;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final OutPlayerService outPlayerService;

    @RequestMapping(request = "야구장등록")
    public void saveStadium(final Request request)
            throws BadRequestException, StadiumRegistrationFailureException, SQLException {

        StadiumRequestDTO stadiumRequest = StadiumRequestDTO.from(request);
        StadiumResponseDTO response = stadiumService.save(stadiumRequest);
        View.printResponse(response);
    }

    @RequestMapping(request = "야구장목록")
    public void viewAllStadiums(final Request request)
            throws BadRequestException, StadiumFindFailureException, SQLException {

        if (!Objects.isNull(request.getBody())) throw new BadRequestException();

        List<StadiumResponseDTO> allStadiums = stadiumService.findAll();
        View.printResponse(allStadiums);
    }

    @RequestMapping(request = "팀등록")
    public void saveTeam(final Request request)
            throws BadRequestException, TeamRegistrationFailureException, SQLException {

        TeamRequestDTO teamRequest = TeamRequestDTO.from(request);
        TeamResponseDTO response = teamService.save(teamRequest);
        View.printResponse(response);
    }

    @RequestMapping(request = "팀목록")
    public void viewAllTeams(final Request request)
            throws BadRequestException, TeamFindFailureException, SQLException {

        if (!Objects.isNull(request.getBody())) throw new BadRequestException();
        List<TeamWithStadiumResponseDTO> allTeamWithStadium = teamService.findAllWithStadium();
        View.printResponse(allTeamWithStadium);
    }

    @RequestMapping(request = "선수목록")
    public void viewPlayersByTeam(final Request request)
            throws BadRequestException, FindPlayersFailureException, SQLException {

        PlayerDTO.FindPlayersByTeamRequest findPlayersByTeamRequest = PlayerDTO.FindPlayersByTeamRequest.from(request);
        List<PlayerDTO.FindPlayerResponse> response = playerService.findByTeam(findPlayersByTeamRequest);
        View.printResponse(response);

    }

    @RequestMapping(request = "퇴출목록")
    public void viewOutPlayers(final Request request)
            throws BadRequestException, FindPlayersFailureException, SQLException {

        List<OutPlayerDTO.FindOutPlayerResponse> response = outPlayerService.findOutPlayers();
        View.printResponse(response);
    }

    @RequestMapping(request = "포지션별목록")
    public void viewPlayersGroupByPosition(final Request request)
            throws BadRequestException, FindPlayersFailureException, SQLException {

        List<PlayerDTO.FindPlayerGroupByPositionResponse> response = playerService.findPlayerGroupByPosition();
        View.printResponseAsPivot(response, "teamName", "position", "name");
    }

    @RequestMapping(request = "선수등록")
    public void savePlayer(final Request request)
            throws BadRequestException, PlayerRegistrationFailureException, SQLException {

        PlayerDTO.NewPlayerRequest newPlayerRequest = PlayerDTO.NewPlayerRequest.from(request);
        PlayerDTO.FindPlayerResponse response = playerService.save(newPlayerRequest);
        View.printResponse(response);
    }

    @RequestMapping(request = "퇴출등록")
    public void saveOutPlayer(final Request request)
            throws BadRequestException, RollbackException, PlayerRegistrationFailureException, SQLException {

        OutPlayerDTO.NewOutPlayerRequest newOutPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.from(request);
        OutPlayerDTO.FindOutPlayerResponse response = outPlayerService.save(newOutPlayerRequest);
        View.printResponse(response);
    }
}
