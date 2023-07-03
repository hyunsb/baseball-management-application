package exception;

public class TeamFindFailureException extends ServiceFailureException {
    public TeamFindFailureException(String message) {
        super("[팀 검색 실패] " + message);
    }
}
