package util;

import domain.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class RequestParserTest {

    @DisplayName("파싱 성공 테스트 - 쿼리 스트링 형식")
    @Test
    void parse_Success_With_QueryString_Test() {
        // Given
        String consoleRequest = "선수등록?teamId=1&name=이대호&position=1루수";

        String expectedHeader = "선수등록";
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("teamId", "1");
        expectedBody.put("name", "이대호");
        expectedBody.put("position", "1루수");

        // When
        Request request = RequestParser.parse(consoleRequest);
        String actualHeader = request.getHeader();
        Map<String, String> actualBody = request.getBody();

        // Then
        Assertions.assertEquals(expectedHeader, actualHeader);
        Assertions.assertEquals(expectedBody, actualBody);
    }

    @DisplayName("파싱 실패 테스트 - 쿼리 스트링 형식")
    @Test
    void parse_Failed_With_QueryString_Test() {
        // Given
        String consoleBadRequest = "선수등록?teamId=1&name==이대호&position=1루수";

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> RequestParser.parse(consoleBadRequest));
    }

    @DisplayName("파싱 실패 테스트 - 쿼리 스트링 형식")
    @Test
    void parse_Failed_With_QueryString_Test2() {
        // Given
        String consoleBadRequest = "선수등록1?teamId=1&name이대호&position=1루수";

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> RequestParser.parse(consoleBadRequest));
    }

    @DisplayName("파싱 성공 테스트 - 일반 형식")
    @Test
    void parse_Success_Without_QueryString_Test() {
        // Given
        String consoleRequest = "선수목록";

        // When
        String expectedHeader = "선수목록";
        Request actual = RequestParser.parse(consoleRequest);

        // Then
        Assertions.assertEquals(expectedHeader, actual.getHeader());
        Assertions.assertNull(actual.getBody());
    }

    @DisplayName("파싱 실패 테스트 - 일반 형식")
    @Test
    void parse_Failed_Without_QueryString_Test() {
        // Given
        String consoleBadRequest = "선수목록?";

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RequestParser.parse(consoleBadRequest);
        });
    }
}