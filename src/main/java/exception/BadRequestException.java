package exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("[BAD REQUEST] 요청 형식이 올바르지 않습니다.");
    }

    public BadRequestException(String message) {
        super("[BAD REQUEST] " + message);
    }
}
