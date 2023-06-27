package util;

import domain.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class RequestParserTest {

    @DisplayName("파싱 성공 테스트")
    @Test
    void parse_Success_Test() {
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

    @DisplayName("파싱 실패 테스트")
    @Test
    void parse_Failed_Test() {
        // Given
        String consoleBadRequest = "선수등록?teamId=1&name==이대호&position=1루수";

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> RequestParser.parse(consoleBadRequest));
    }
}