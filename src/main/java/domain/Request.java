package domain;

import java.util.Map;

public class Request {

    private final String header;
    private Map<String, String> body;

    public Request(String header) {
        this.header = header;
    }

    public Request(String header, Map<String, String> body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
