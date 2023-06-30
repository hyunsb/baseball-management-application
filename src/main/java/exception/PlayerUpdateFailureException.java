package exception;

public class PlayerUpdateFailureException extends RuntimeException {
    public PlayerUpdateFailureException(String message) {
        super(message);
    }
}
