package exception;

public class PlayerRegistrationFailureException extends RuntimeException {
    public PlayerRegistrationFailureException(String message) {
        super(message);
    }
}
