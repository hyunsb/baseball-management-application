package util;

import domain.Position;
import dto.player.PlayerDTO;
import dto.team.TeamResponse;
import org.junit.jupiter.api.Test;

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
        ResponseDTOPrinter.printResponseDTO(teamResponse);

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
        ResponseDTOPrinter.printResponseDTO(findPlayerResponseList);

        //then
        //check console
    }
}