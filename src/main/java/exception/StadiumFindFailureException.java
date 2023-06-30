package exception;

public class StadiumFindFailureException extends ServiceFailureException {
    public StadiumFindFailureException(String message) {
        super("[야구장 검색 실패] " + message);
    }
}
