package exception;

public class FindPlayersFailureException extends ServiceFailureException {
    public FindPlayersFailureException(String message) {
        super(message);
    }
}
