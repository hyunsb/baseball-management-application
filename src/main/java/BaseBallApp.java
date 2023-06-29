import annotation.RequestMapping;
import dao.StadiumDAO;
import dao.TeamDAO;
import db.DBConnection;
import domain.Request;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
import dto.team.TeamRequest;
import dto.team.TeamResponse;
import dto.team.TeamWithStadiumResponse;
import exception.*;
import service.StadiumService;
import service.TeamService;
import view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class BaseBallApp {

    private static final StadiumService STADIUM_SERVICE;
    private static final TeamService TEAM_SERVICE;

    // inject dependency
    static {
        Connection connection = DBConnection.getInstance();

        StadiumDAO stadiumDAO = new StadiumDAO(connection);
        TeamDAO teamDAO = new TeamDAO(connection);

        STADIUM_SERVICE = new StadiumService(stadiumDAO);
        TEAM_SERVICE = new TeamService(teamDAO, stadiumDAO);
    }

    public static void main(String[] args) {
        while (true) {
            try {
                Request request = View.inputRequest();
                mappingRequest(request);
            } catch (IllegalAccessException | InvocationTargetException | BadRequestException exception) {
                View.printErrorMessage(exception.getMessage());
            }
        }
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
    private static void saveStadium(final Request request) {
        try {
            StadiumRequest stadiumRequest = StadiumRequest.from(request);
            StadiumResponse response = STADIUM_SERVICE.save(stadiumRequest);
            View.printResponse(request.getHeader(), response);

        } catch (StadiumRegistrationFailureException | BadRequestException exception) {
            View.printErrorMessage(exception.getMessage());
        }
    }

    @RequestMapping(request = "야구장목록")
    private static void viewAllStadiums(final Request request) {
        try {
            if (!Objects.isNull(request.getBody())) throw new BadRequestException();

            List<StadiumResponse> allStadiums = STADIUM_SERVICE.findAll();
            View.printResponse(request.getHeader(), allStadiums);

        } catch (StadiumFindFailureException | BadRequestException exception) {
            View.printErrorMessage(exception.getMessage());
        }
    }

    @RequestMapping(request = "팀등록")
    private static void saveTeam(final Request request) {
        try {
            TeamRequest teamRequest = TeamRequest.from(request);
            TeamResponse response = TEAM_SERVICE.save(teamRequest);
            View.printResponse(request.getHeader(), response);

        } catch (TeamRegistrationFailureException | BadRequestException exception) {
            View.printErrorMessage(exception.getMessage());
        }
    }

    @RequestMapping(request = "팀목록")
    private static void viewAllTeams(final Request request) {
        try {
            if (!Objects.isNull(request.getBody())) throw new BadRequestException();
            List<TeamWithStadiumResponse> allTeamWithStadium = TEAM_SERVICE.findAllWithStadium();
            View.printResponse(request.getHeader(), allTeamWithStadium);

        } catch (TeamFindFailureException | BadRequestException exception) {
            View.printErrorMessage(exception.getMessage());
        }
    }
}
