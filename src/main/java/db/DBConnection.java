package db;

import exception.DBConnectException;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Suppresses default constructor, ensuring non-instantiability
    private DBConnection() {
    }

    public static Connection getInstance() {
        String url = DBConnectInfo.URL.value();
        String username = DBConnectInfo.USER_NAME.value();
        String password = DBConnectInfo.PASSWORD.value();

        try {
            Class.forName(DBConnectInfo.DRIVER.value());
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new DBConnectException();
    }
}
