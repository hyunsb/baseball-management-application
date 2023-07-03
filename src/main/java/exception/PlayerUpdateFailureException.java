package exception;

public class PlayerUpdateFailureException extends ServiceFailureException {
    public PlayerUpdateFailureException(String message) {
        super(message);
    }
}
