package exception;

public class PlayerRegistrationFailureException extends ServiceFailureException {
    public PlayerRegistrationFailureException(String message) {
        super(message);
    }
}
