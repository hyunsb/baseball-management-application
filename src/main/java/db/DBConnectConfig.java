package db;

public enum DBConnectConfig {

    URL("jdbc:mysql://localhost:3306/baseball"),
    USER_NAME("root"),
    PASSWORD("1234"),
    DRIVER("com.mysql.cj.jdbc.Driver");

    private final String value;

    DBConnectConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
