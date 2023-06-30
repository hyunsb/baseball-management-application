package exception;

public class DBConnectException extends RuntimeException {

    public DBConnectException() {
        super("DB 연결 정보가 올바르지 않습니다.");
    }

    public DBConnectException(String message) {
        super(message);
    }
}
