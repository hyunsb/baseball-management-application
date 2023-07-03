package service;

import core.ConnectionPoolManager;
import dao.PlayerDAO;
import dao.StadiumDAO;
import dao.TeamDAO;
import db.Sql;
import domain.Position;
import dto.player.PlayerDTO;
import dto.team.TeamRequestDTO;
import exception.PlayerRegistrationFailureException;
import org.junit.jupiter.api.*;
import view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


class PlayerServiceTest {

    private static final ConnectionPoolManager CONNECTION_POOL_MANAGER = ConnectionPoolManager.getInstance();
    private static final PlayerDAO playerDao = new PlayerDAO();
    private static final TeamDAO teamDAO = new TeamDAO();
    private static final StadiumDAO stadiumDAO = new StadiumDAO();

    private static final PlayerService playerService = new PlayerService(CONNECTION_POOL_MANAGER, playerDao);
    private static final TeamService teamService = new TeamService(CONNECTION_POOL_MANAGER, teamDAO, stadiumDAO);

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = CONNECTION_POOL_MANAGER.getConnection();
        connection.prepareStatement(Sql.PLAYER.getCreate()).execute();
        connection.prepareStatement(Sql.TEAM.getCreate()).execute();
    }

    @AfterEach
    void cleanUP() throws SQLException {
        Connection connection = CONNECTION_POOL_MANAGER.getConnection();
        connection.prepareStatement(Sql.PLAYER.getDrop()).execute();
        connection.prepareStatement(Sql.TEAM.getDrop()).execute();
    }

    @DisplayName("선수 등록 성공")
    @Test
    void save_success_test() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = new PlayerDTO.NewPlayerRequest(1L, "나포수", Position.valueOf("C").getValue());

        //when
        PlayerDTO.FindPlayerResponse findPlayerResponse = playerService.save(playerRequest);

        //then
        Assertions.assertEquals("나포수", findPlayerResponse.getName());
    }

    @DisplayName("선수 등록 성공 - 다른 팀 같은 포지션")
    @Test
    void save_success_test_differentTeam_same_position() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest1 = new PlayerDTO.NewPlayerRequest(1L, "1팀포수", Position.valueOf("C").getValue());
        PlayerDTO.NewPlayerRequest playerRequest2 = new PlayerDTO.NewPlayerRequest(2L, "2팀포수", Position.valueOf("C").getValue());


        //when
        PlayerDTO.FindPlayerResponse findPlayerResponse1 = playerService.save(playerRequest1);
        PlayerDTO.FindPlayerResponse findPlayerResponse2 = playerService.save(playerRequest2);

        //then
        Assertions.assertEquals("1팀포수", findPlayerResponse1.getName());
        Assertions.assertEquals("2팀포수", findPlayerResponse2.getName());
    }

    @DisplayName("선수 등록 실패 - 중복 선수")
    @Test
    void save_fail_test_same_player() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();

        playerService.save(playerRequest);

        //when

        //then
        Assertions.assertThrows(PlayerRegistrationFailureException.class, () -> playerService.save(playerRequest));
    }

    @DisplayName("선수 등록 실패 - 중복 포지션")
    @Test
    void save_fail_test_same_position() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest1 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();

        PlayerDTO.NewPlayerRequest playerRequest2 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나도포수")
                .position(Position.valueOf("C").getValue())
                .build();

        playerService.save(playerRequest1);

        //when

        //then
        Assertions.assertThrows(PlayerRegistrationFailureException.class, () -> playerService.save(playerRequest2));
    }

    @DisplayName("선수 업데이트 성공")
    @Test
    void updatePlayer_success_test() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();
        playerService.save(playerRequest);

        //when
        playerService.update(new PlayerDTO.UpdatePlayerTeamIdForOutRequest(1L));

        //then
        Assertions.assertEquals(-1, playerDao.findById(CONNECTION_POOL_MANAGER.getConnection(), 1L).get().getTeamId());
    }

    @DisplayName("팀별 선수 목록 성공")
    @Test
    void findByTeam_success_test() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest1 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("1팀포수")
                .position(Position.valueOf("C").getValue())
                .build();

        PlayerDTO.NewPlayerRequest playerRequest2 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("1팀투수")
                .position(Position.valueOf("P").getValue())
                .build();

        PlayerDTO.NewPlayerRequest playerRequest3 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(2L)
                .name("2팀포수")
                .position(Position.valueOf("C").getValue())
                .build();

        playerService.save(playerRequest1);
        playerService.save(playerRequest2);
        playerService.save(playerRequest3);

        PlayerDTO.FindPlayersByTeamRequest findPlayersByTeamRequest1 = new PlayerDTO.FindPlayersByTeamRequest(1L);
        PlayerDTO.FindPlayersByTeamRequest findPlayersByTeamRequest2 = new PlayerDTO.FindPlayersByTeamRequest(2L);
        PlayerDTO.FindPlayersByTeamRequest findPlayersByTeamRequest3 = new PlayerDTO.FindPlayersByTeamRequest(3L);

        //when
        List<PlayerDTO.FindPlayerResponse> playerList1 = playerService.findByTeam(findPlayersByTeamRequest1);
        List<PlayerDTO.FindPlayerResponse> playerList2 = playerService.findByTeam(findPlayersByTeamRequest2);
        List<PlayerDTO.FindPlayerResponse> playerList3 = playerService.findByTeam(findPlayersByTeamRequest3);

        //then
        Assertions.assertEquals(2, playerList1.size());
        Assertions.assertEquals(1, playerList2.size());
        Assertions.assertEquals(0, playerList3.size());

    }

    @DisplayName("포지션 별 선수 페이지 성공 테스트")
    @Test
    void findPlayerGroupByPosition() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest1 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("1팀포수")
                .position(Position.valueOf("C").getValue())
                .build();

        PlayerDTO.NewPlayerRequest playerRequest2 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("1팀투수")
                .position(Position.valueOf("P").getValue())
                .build();

        PlayerDTO.NewPlayerRequest playerRequest3 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(2L)
                .name("2팀포수")
                .position(Position.valueOf("C").getValue())
                .build();

        playerService.save(playerRequest1);
        playerService.save(playerRequest2);
        playerService.save(playerRequest3);

        TeamRequestDTO teamRequest1 = new TeamRequestDTO("TeamA", 1L);
        TeamRequestDTO teamRequest2 = new TeamRequestDTO("TeamB", 2L);

        teamService.save(teamRequest1);
        teamService.save(teamRequest2);

        //when
        List<PlayerDTO.FindPlayerGroupByPositionResponse> response = playerService.findPlayerGroupByPosition();
        View.printResponse(response);

        //then
        Assertions.assertEquals(3, response.size());

    }
}