package exception;

public class DBConnectException extends RuntimeException {

    public DBConnectException() {
        super(ErrorMessage.INVALID_DB_CONNECTION);
    }

    public DBConnectException(String message) {
        super(message);
    }
}
