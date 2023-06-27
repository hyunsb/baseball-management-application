package domain;

public enum Position {

    P("투수"), C("포수"),
    B("1루수"), B2("2루수"), B3("3루수"), SS("유격수"),
    LF("좌익수"), CF("중견수"), RF("우익수");

    private final String position;

    Position(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
