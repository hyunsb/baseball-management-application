package util;

import domain.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    // Suppresses default constructor, ensuring non-instantiability
    private RequestParser() {
    }

    public static Request parse(String request) {
        String header = request.substring(0, request.indexOf('?'));
        String bodyString = request.substring(request.indexOf('?') + 1);
        Map<String, String> body = parseBody(bodyString);

        return new Request(header, body);
    }

    private static Map<String, String> parseBody(String bodyString) {
        Map<String, String> body = new HashMap<>();

        String[] bodyPairs = bodyString.split("&");
        for (String bodyPair : bodyPairs) {
            String[] keyValue = bodyPair.split("=");

            if (keyValue.length != 2) throw new IllegalArgumentException("Bad Request");
            String key = keyValue[0];
            String value = keyValue[1];
            body.put(key, value);
        }

        return body;
    }
}
