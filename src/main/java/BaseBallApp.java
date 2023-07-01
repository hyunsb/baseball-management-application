import annotation.RequestMapping;
import dao.OutPlayerDAO;
import dao.PlayerDAO;
import dao.StadiumDAO;
import dao.TeamDAO;
import domain.Request;
import dto.player.OutPlayerDTO;
import dto.player.PlayerDTO;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
import dto.team.TeamRequest;
import dto.team.TeamResponse;
import dto.team.TeamWithStadiumResponse;
import exception.*;
import service.OutPlayerService;
import service.PlayerService;
import service.StadiumService;
import service.TeamService;
import view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class BaseBallApp {

    private static StadiumService STADIUM_SERVICE;
    private static TeamService TEAM_SERVICE;
    private static PlayerService PLAYER_SERVICE;
    private static OutPlayerService OUT_PLAYER_SERVICE;

    private final Connection connection;

    public BaseBallApp(Connection connection) {
        this.connection = connection;

        // 임시코드 -> 싱글톤으로 변경할 것
        StadiumDAO stadiumDAO = new StadiumDAO(connection);
        TeamDAO teamDAO = new TeamDAO(connection);
        PlayerDAO playerDAO = new PlayerDAO(connection);
        OutPlayerDAO outPlayerDAO = new OutPlayerDAO(connection);

        STADIUM_SERVICE = new StadiumService(stadiumDAO);
        TEAM_SERVICE = new TeamService(teamDAO, stadiumDAO);
        PLAYER_SERVICE = new PlayerService(playerDAO);
        OUT_PLAYER_SERVICE = new OutPlayerService(outPlayerDAO, playerDAO, connection);

        run();
    }

    public static void mappingRequest(final Request request)
            throws IllegalAccessException, InvocationTargetException, BadRequestException {

        Method[] methods = BaseBallApp.class.getDeclaredMethods();

        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            if (Objects.isNull(requestMapping)) continue;

            if (Objects.equals(requestMapping.request(), request.getHeader())) {
                method.invoke(BaseBallApp.class, request);
                return;
            }
        }
        throw new BadRequestException("요청 형식이 올바르지 않습니다.");
    }

    @RequestMapping(request = "야구장등록")
    private static void saveStadium(final Request request)
            throws BadRequestException, StadiumRegistrationFailureException {

        StadiumRequest stadiumRequest = StadiumRequest.from(request);
        StadiumResponse response = STADIUM_SERVICE.save(stadiumRequest);
        View.printResponse(response);
    }

    @RequestMapping(request = "야구장목록")
    private static void viewAllStadiums(final Request request)
            throws BadRequestException, StadiumFindFailureException {

        if (!Objects.isNull(request.getBody())) throw new BadRequestException();

        List<StadiumResponse> allStadiums = STADIUM_SERVICE.findAll();
        View.printResponse(allStadiums);
    }

    @RequestMapping(request = "팀등록")
    private static void saveTeam(final Request request)
            throws BadRequestException, TeamRegistrationFailureException {

        TeamRequest teamRequest = TeamRequest.from(request);
        TeamResponse response = TEAM_SERVICE.save(teamRequest);
        View.printResponse(response);
    }

    @RequestMapping(request = "팀목록")
    private static void viewAllTeams(final Request request)
            throws BadRequestException, TeamFindFailureException {

        if (!Objects.isNull(request.getBody())) throw new BadRequestException();
        List<TeamWithStadiumResponse> allTeamWithStadium = TEAM_SERVICE.findAllWithStadium();
        View.printResponse(allTeamWithStadium);
    }

    @RequestMapping(request = "선수목록")
    private static void viewPlayersByTeam(final Request request)
            throws BadRequestException, FindPlayersFailureException {

        PlayerDTO.FindPlayersByTeamRequest findPlayersByTeamRequest = PlayerDTO.FindPlayersByTeamRequest.from(request);
        List<PlayerDTO.FindPlayerResponse> response = PLAYER_SERVICE.findByTeam(findPlayersByTeamRequest);
        View.printResponse(response);

    }

    @RequestMapping(request = "퇴출목록")
    private static void viewOutPlayers(final Request request)
            throws BadRequestException, FindPlayersFailureException {

        List<OutPlayerDTO.FindOutPlayerResponse> response = OUT_PLAYER_SERVICE.findOutPlayers();
        View.printResponse(response);
    }

    @RequestMapping(request = "포지션별목록")
    private static void viewPlayersGroupByPosition(final Request request)
            throws BadRequestException, FindPlayersFailureException {

        List<PlayerDTO.FindPlayerGroupByPositionResponse> response = PLAYER_SERVICE.findPlayerGroupByPosition();
        View.printResponseAsPivot(response, "teamName", "position", "name");
    }

    @RequestMapping(request = "선수등록")
    private static void savePlayer(final Request request)
            throws BadRequestException, PlayerRegistrationFailureException {

        PlayerDTO.NewPlayerRequest newPlayerRequest = PlayerDTO.NewPlayerRequest.from(request);
        PlayerDTO.FindPlayerResponse response = PLAYER_SERVICE.save(newPlayerRequest);
        View.printResponse(response);
    }

    @RequestMapping(request = "퇴출등록")
    private static void saveOutPlayer(final Request request)
            throws BadRequestException, RollbackException, PlayerRegistrationFailureException {

        OutPlayerDTO.NewOutPlayerRequest newOutPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.from(request);
        OutPlayerDTO.FindOutPlayerResponse response = OUT_PLAYER_SERVICE.save(newOutPlayerRequest);
        View.printResponse(response);
    }

    public void run() {
        while (true) {
            try {
                Request request = View.inputRequest();
                mappingRequest(request);
            } catch (BadRequestException | ServiceFailureException | RollbackException exception) {
                View.printErrorMessage(exception.getMessage());
            } catch (IllegalAccessException | InvocationTargetException exception) {
                View.printErrorMessage(exception.getCause().toString());
            }
        }
    }
}
