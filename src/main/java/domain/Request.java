package domain;

import java.util.Map;

public class Request {

    private final String header;
    private final Map<String, String> body;

    public Request(String header, Map<String, String> body) {
        validateHeader(header);
        validateBody(body);
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    private void validateHeader(String header) {

    }

    private void validateBody(Map<String, String> body) {

    }
}
