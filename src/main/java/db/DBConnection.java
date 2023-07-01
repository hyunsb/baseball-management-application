package db;

import exception.DBConnectException;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Suppresses default constructor, ensuring non-instantiability
    private DBConnection() {
    }

    public static Connection getInstance(String url, String username, String password, String driver) throws DBConnectException {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new DBConnectException(e.getMessage());
        }
    }
}
