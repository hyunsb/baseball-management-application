package service;

import dao.PlayerDAO;
import db.DBConnection;
import db.Sql;
import domain.Position;
import dto.player.PlayerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Exception.PlayerRegistrationFailureException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


class PlayerServiceTest {

    private static final Connection connection = DBConnection.getInstance();
    private static final PlayerDAO playerDao = new PlayerDAO(connection);
    private static final PlayerService playerService = new PlayerService(playerDao);

    @BeforeEach
    void setUp() throws SQLException {
        connection.prepareStatement(Sql.PLAYER.getDrop()).execute();
        connection.prepareStatement(Sql.PLAYER.getCreate()).execute();
    }

    @DisplayName("선수 등록 성공")
    @Test
    void save_success_test() {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();

        //when
        PlayerDTO.FindPlayerResponse findPlayerResponse = playerService.save(playerRequest);

        //then
        Assertions.assertEquals("나포수", findPlayerResponse.getName());
    }

    @DisplayName("선수 등록 성공 - 다른 팀 같은 포지션")
    @Test
    void save_success_test_differentTeam_same_position() {
        //given
        PlayerDTO.NewPlayerRequest playerRequest1 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("1팀포수")
                .position(Position.valueOf("C").getValue())
                .build();

        PlayerDTO.NewPlayerRequest playerRequest2 = PlayerDTO.NewPlayerRequest.builder()
                .teamId(2L)
                .name("2팀포수")
                .position(Position.valueOf("C").getValue())
                .build();

        //when
        PlayerDTO.FindPlayerResponse findPlayerResponse1 = playerService.save(playerRequest1);
        PlayerDTO.FindPlayerResponse findPlayerResponse2 = playerService.save(playerRequest2);

        //then
        Assertions.assertEquals("1팀포수", findPlayerResponse1.getName());
        Assertions.assertEquals("2팀포수", findPlayerResponse2.getName());
    }

    @DisplayName("선수 등록 실패 - 중복 선수")
    @Test
    void save_fail_test_same_player() {
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
    void save_fail_test_same_position() {
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

    @DisplayName("팀별 선수 목록 성공")
    @Test
    void findByTeam_success_test() {
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

}