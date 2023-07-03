package exception;

public class StadiumRegistrationFailureException extends ServiceFailureException {
    public StadiumRegistrationFailureException(String message) {
        super("[야구장 등록 실패] " + message);
    }
}
