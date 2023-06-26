package Exception;

public class PlayerUpdateFailureException extends RuntimeException {
    public PlayerUpdateFailureException(String message) {
        super(message);
    }
}
