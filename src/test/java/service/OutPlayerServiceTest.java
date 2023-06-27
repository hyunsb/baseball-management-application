package service;

import Exception.PlayerRegistrationFailureException;
import dao.OutPlayerDAO;
import dao.PlayerDAO;
import db.DBConnection;
import db.Sql;
import domain.Position;
import domain.Reason;
import dto.player.OutPlayerDTO;
import dto.player.PlayerDTO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class OutPlayerServiceTest {

    private static final Connection connection = DBConnection.getInstance();
    private static final PlayerDAO playerDao = new PlayerDAO(connection);
    private static final PlayerService playerService = new PlayerService(playerDao);
    private static final OutPlayerDAO outPlayerDao = new OutPlayerDAO(connection);
    private static final OutPlayerService outPlayerService = new OutPlayerService(outPlayerDao, playerDao, connection);

    @BeforeEach
    void setUp() throws SQLException {
        connection.prepareStatement(Sql.OUT_PLAYER.getCreate()).execute();
        connection.prepareStatement(Sql.PLAYER.getCreate()).execute();
    }

    @AfterEach
    void clenUp() throws SQLException {
        connection.prepareStatement(Sql.OUT_PLAYER.getDrop()).execute();
        connection.prepareStatement(Sql.PLAYER.getDrop()).execute();
    }

    @DisplayName("퇴출 선수 등록 성공")
    @Test
    void save_success_test() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();

        OutPlayerDTO.NewOutPlayerRequest outPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.builder()
                .reason(Reason.valueOf("GAMBLING").getValue())
                .playerId(1L)
                .build();

        playerService.save(playerRequest);

        //when
        OutPlayerDTO.FindOutPlayerResponse findoutPlayerResponse = outPlayerService.save(outPlayerRequest);

        //then
        Assertions.assertEquals("나포수", findoutPlayerResponse.getName());
        Assertions.assertEquals("도박", findoutPlayerResponse.getReason());
    }

    @DisplayName("퇴출 선수 등록 실패 - 중복 선수")
    @Test
    void save_fail_test_same_player() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();

        OutPlayerDTO.NewOutPlayerRequest outPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.builder()
                .reason(Reason.valueOf("GAMBLING").getValue())
                .playerId(1L)
                .build();

        playerService.save(playerRequest);
        outPlayerService.save(outPlayerRequest);

        //when

        //then
        Assertions.assertThrows(PlayerRegistrationFailureException.class, () -> outPlayerService.save(outPlayerRequest));
    }

    @DisplayName("퇴출 선수 등록 실패 - 존재 하지 않는 선수")
    @Test
    void save_fail_test_invalid_player() {
        //given
        OutPlayerDTO.NewOutPlayerRequest outPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.builder()
                .reason(Reason.valueOf("GAMBLING").getValue())
                .playerId(1L)
                .build();
        //when

        //then
        Assertions.assertThrows(PlayerRegistrationFailureException.class, () -> outPlayerService.save(outPlayerRequest));
    }

    @DisplayName("퇴출 선수 등록 Transaction 테스트")
    @Test
    void save_fail_verifying_of_transaction() throws SQLException {
        //given
        PlayerDTO.NewPlayerRequest playerRequest = PlayerDTO.NewPlayerRequest.builder()
                .teamId(1L)
                .name("나포수")
                .position(Position.valueOf("C").getValue())
                .build();

        playerService.save(playerRequest);

        OutPlayerDTO.NewOutPlayerRequest outPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.builder()
                .reason(Reason.valueOf("GAMBLING").getValue())
                .playerId(2L)
                .build();

        //when


        //then
        Assertions.assertThrows(PlayerRegistrationFailureException.class, () -> outPlayerService.save(outPlayerRequest));
        Assertions.assertEquals(1L, playerDao.findById(1L).get().getTeamId());
    }

    @DisplayName("퇴출 선수 목록")
    @Test
    void findOutPlayers() {
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

        OutPlayerDTO.NewOutPlayerRequest outPlayerRequest = OutPlayerDTO.NewOutPlayerRequest.builder()
                .reason(Reason.valueOf("GAMBLING").getValue())
                .playerId(1L)
                .build();


        outPlayerService.save(outPlayerRequest);

        //when
        List<OutPlayerDTO.FindOutPlayerResponse> outPlayers = outPlayerService.findOutPlayers();

        //then
        Assertions.assertEquals(1, outPlayers.size());
        Assertions.assertEquals("1팀포수", outPlayers.get(0).getName());
    }
}