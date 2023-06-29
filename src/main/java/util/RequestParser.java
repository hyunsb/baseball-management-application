package util;

import domain.Request;
import exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestParser {

    // "야구장등록?name=잠실야구장"과 같은 쿼리스트링이 포함된 요청 패턴
    private static final Pattern REQUEST_WITH_QUERY_STRING_PATTERN
            = Pattern.compile("^[가-힣]+\\?(?:[a-zA-Z0-9_-]+=[a-zA-Z0-9%_-]*&?)*$");

    // "야구장목록"과 같은 쿼리스트링이 포함되지 않은 요청 패턴 (한글만 허용)
    private static final Pattern REQUEST_WITHOUT_QUERY_STRING_PATTERN
            = Pattern.compile("[가-힣]");

    // Suppresses default constructor, ensuring non-instantiability
    private RequestParser() {
    }

    public static Request parse(String request) throws BadRequestException {
        validateRequest(request);

        // 쿼리스트링 형식이 아닌 경우
        if (!request.contains("?"))
            return new Request(request);

        String header = request.substring(0, request.indexOf('?'));
        String bodyString = request.substring(request.indexOf('?') + 1);
        Map<String, String> body = parseBody(bodyString);

        return new Request(header, body);
    }

    private static void validateRequest(String request) throws BadRequestException {
        if (!(REQUEST_WITH_QUERY_STRING_PATTERN.matcher(request).find()
                || REQUEST_WITHOUT_QUERY_STRING_PATTERN.matcher(request).find()))
            throw new BadRequestException();
    }

    private static Map<String, String> parseBody(String bodyString) throws BadRequestException{
        Map<String, String> body = new HashMap<>();

        String[] bodyPairs = bodyString.split("&");
        for (String bodyPair : bodyPairs) {
            String[] keyValue = bodyPair.split("=");
            if (keyValue.length != 2) throw new BadRequestException();
            body.put(keyValue[0], keyValue[1]);
        }

        return body;
    }
}
