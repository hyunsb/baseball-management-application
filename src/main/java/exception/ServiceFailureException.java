package exception;

public class ServiceFailureException extends RuntimeException {
    
    public ServiceFailureException() {
    }

    public ServiceFailureException(String message) {
        super(message);
    }
}
