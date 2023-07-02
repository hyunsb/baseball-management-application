package util;

import core.ConnectionPoolManager;
import dao.OutPlayerDAO;
import dao.PlayerDAO;

import db.DBConnection;
import domain.Position;
import dto.player.OutPlayerDTO;
import dto.player.PlayerDTO;
import dto.team.TeamResponse;
import model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OutPlayerService;

import view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ResponseDTOPrinterTest {

    @Test
    void printResponseDTO() {
        //given
        TeamResponse teamResponse = TeamResponse.builder()
                .id(1L)
                .name("롯데")
                .stadiumId(1L)
                .createAt(Timestamp.valueOf(LocalDateTime.now())).build();

        //when
        View.ResponseDTOPrinter.printResponseDTO(teamResponse);

        //then
        //check console
    }

    @Test
    void testPrintResponseDTO() {
        //given
        PlayerDTO.FindPlayerResponse response1 = PlayerDTO.FindPlayerResponse.builder()
                .id(1L)
                .name("아무개")
                .position(Position.C.getValue())
                .build();
        PlayerDTO.FindPlayerResponse response2 = PlayerDTO.FindPlayerResponse.builder()
                .id(2L)
                .name("홍길동")
                .position(Position.C.getValue())
                .build();
        PlayerDTO.FindPlayerResponse response3 = PlayerDTO.FindPlayerResponse.builder()
                .id(3L)
                .name("가나다")
                .position(Position.C.getValue())
                .build();
        PlayerDTO.FindPlayerResponse response4 = PlayerDTO.FindPlayerResponse.builder()
                .id(4L)
                .name("차카파")
                .position(Position.C.getValue())
                .build();
        PlayerDTO.FindPlayerResponse response5 = PlayerDTO.FindPlayerResponse.builder()
                .id(5L)
                .name("마바사")
                .position(Position.C.getValue())
                .build();

        List<PlayerDTO.FindPlayerResponse> findPlayerResponseList = new ArrayList<>();
        findPlayerResponseList.add(response1);
        findPlayerResponseList.add(response2);
        findPlayerResponseList.add(response3);
        findPlayerResponseList.add(response4);
        findPlayerResponseList.add(response5);

        //when
        View.ResponseDTOPrinter.printResponseDTO(findPlayerResponseList);

        //then
        //check console
    }

    @DisplayName("퇴출목록 print")
    @Test
    public void printOutPlayers() throws SQLException {
        PlayerDAO playerDAO = new PlayerDAO();
        OutPlayerDAO outPlayerDAO = new OutPlayerDAO();

        OutPlayerService outPlayerService = new OutPlayerService(ConnectionPoolManager.getInstance(), outPlayerDAO, playerDAO);
        List<OutPlayerDTO.FindOutPlayerResponse> responses = outPlayerService.findOutPlayers();
        View.printResponse(responses);

    }

    @DisplayName("Pivot table print test")
    @Test
    void createPivotTable() {
        //given
        PlayerDTO.FindPlayerGroupByPositionResponse response1 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("아무개")
                        .teamId(1L)
                        .position(Position.valueOf("C"))
                        .teamName("롯데")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response2 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("가나다")
                        .teamId(1L)
                        .position(Position.valueOf("P"))
                        .teamName("롯데")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response3 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("라마바")
                        .teamId(1L)
                        .position(Position.valueOf("CF"))
                        .teamName("롯데")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response4 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("사아자")
                        .teamId(2L)
                        .position(Position.valueOf("C"))
                        .teamName("삼성")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response5 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("카타파")
                        .teamId(2L)
                        .position(Position.valueOf("RF"))
                        .teamName("삼성")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response6 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("하가나")
                        .teamId(2L)
                        .position(Position.valueOf("RF"))
                        .teamName("SSG")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response7 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("다라마")
                        .teamId(2L)
                        .position(Position.valueOf("LF"))
                        .teamName("한화")
                        .build());
        PlayerDTO.FindPlayerGroupByPositionResponse response8 = PlayerDTO.FindPlayerGroupByPositionResponse.from(
                Player.builder()
                        .name("바사아")
                        .teamId(2L)
                        .position(Position.valueOf("B2"))
                        .teamName("두산")
                        .build());
        List<PlayerDTO.FindPlayerGroupByPositionResponse> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);
        responseList.add(response3);
        responseList.add(response4);
        responseList.add(response5);
        responseList.add(response6);
        responseList.add(response7);
        responseList.add(response8);

        //when
        View.ResponseDTOPrinter.printPivotTable(responseList, "teamName", "position", "name");

        //then
        //check console
    }
}