package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Suppresses default constructor, ensuring non-instantiability
    private DBConnection() {
    }

    public static Connection getInstance(){
        String url = "jdbc:mysql://localhost:3306/baseball";
        String username = "root";
        String password = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
