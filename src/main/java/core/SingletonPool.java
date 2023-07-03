package core;

import controller.BaseBallApp;
import dao.OutPlayerDAO;
import dao.PlayerDAO;
import dao.StadiumDAO;
import dao.TeamDAO;
import service.OutPlayerService;
import service.PlayerService;
import service.StadiumService;
import service.TeamService;

public class SingletonPool {

    private static final ConnectionPoolManager CONNECTION_POOL_MANAGER = ConnectionPoolManager.getInstance();

    // DAO
    private static final StadiumDAO STADIUM_DAO = new StadiumDAO();
    private static final TeamDAO TEAM_DAO = new TeamDAO();
    private static final PlayerDAO PLAYER_DAO = new PlayerDAO();
    private static final OutPlayerDAO OUT_PLAYER_DAO = new OutPlayerDAO();

    private static final StadiumService STADIUM_SERVICE = new StadiumService(CONNECTION_POOL_MANAGER, STADIUM_DAO);
    private static final TeamService TEAM_SERVICE = new TeamService(CONNECTION_POOL_MANAGER, TEAM_DAO, STADIUM_DAO);
    private static final PlayerService PLAYER_SERVICE = new PlayerService(CONNECTION_POOL_MANAGER, PLAYER_DAO);
    private static final OutPlayerService OUT_PLAYER_SERVICE = new OutPlayerService(CONNECTION_POOL_MANAGER, OUT_PLAYER_DAO, PLAYER_DAO);

    private static final BaseBallApp BASE_BALL_APP = new BaseBallApp(STADIUM_SERVICE, TEAM_SERVICE, PLAYER_SERVICE, OUT_PLAYER_SERVICE);

    public static BaseBallApp baseBallApp() {
        return BASE_BALL_APP;
    }

    public static ConnectionPoolManager ConnectionPoolManager() {
        return CONNECTION_POOL_MANAGER;
    }
}
