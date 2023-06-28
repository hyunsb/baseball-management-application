package exception;

public class FindPlayersFailureException extends RuntimeException {
    public FindPlayersFailureException(String message) {
        super(message);
    }
}
