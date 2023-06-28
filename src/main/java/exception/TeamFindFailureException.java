package exception;

public class TeamFindFailureException extends RuntimeException {
    public TeamFindFailureException(String message) {
        super("[팀 검색 실패] " + message);
    }
}
