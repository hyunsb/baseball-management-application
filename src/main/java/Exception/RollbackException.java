package Exception;

public class RollbackException extends RuntimeException {
    public RollbackException(String message) {
        super(message);
    }
}
