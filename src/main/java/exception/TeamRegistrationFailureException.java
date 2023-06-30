package exception;

public class TeamRegistrationFailureException extends RuntimeException {
    public TeamRegistrationFailureException(String message) {
        super("[팀 등록 실패] " + message);
    }
}
