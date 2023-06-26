package domain;

public enum Reason implements BasicEnum{

    HEALTH("건강"), PERSONAL("개인사"), RETIREMENT("은퇴"),
    GAMBLING("도박"), PUNISHMENT("징계");

    private final String reason;

    Reason(String reason) {
        this.reason = reason;
    }

    @Override
    public String getValue() {
        return reason;
    }
}
