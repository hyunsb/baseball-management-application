package exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("[BAD REQUEST] " + ErrorMessage.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super("[BAD REQUEST] " + message);
    }
}
