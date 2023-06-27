package Exception;

public class FindPlayersFailureException extends RuntimeException {
    public FindPlayersFailureException(String message) {
        super(message);
    }
}
