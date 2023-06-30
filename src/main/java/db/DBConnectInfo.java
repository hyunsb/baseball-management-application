package db;

public enum DBConnectInfo {

    URL("jdbc:mysql://localhost:3306/baseball"),
    USER_NAME("username"),
    PASSWORD("1234"),
    DRIVER("com.mysql.cj.jdbc.Driver");

    private final String value;

    DBConnectInfo(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
