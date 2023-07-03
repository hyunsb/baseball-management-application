package db;

import exception.DBConnectException;
import exception.ErrorMessage;
import view.View;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 2000;

    // Suppresses default constructor, ensuring non-instantiability
    private DBConnection() {
    }

    public static Connection getInstance(String url, String username, String password, String driver) throws DBConnectException {
        int retires = 0;
        while (MAX_RETRIES > retires) {
            try {
                Class.forName(driver);
                return DriverManager.getConnection(url, username, password);
            } catch (Exception exception) {
                retires++;
                View.printErrorMessage(ErrorMessage.INVALID_DB_CONNECTION);
                View.printErrorMessage(RETRY_DELAY/1000 + ErrorMessage.RE_TRY_DB_CONNECTION + " (" +retires + " / " + MAX_RETRIES + ")");
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException e) {
                    throw new DBConnectException(ErrorMessage.INVALID_DB_CONNECTION);
                }
            }
        }
        throw new DBConnectException(ErrorMessage.INVALID_DB_CONNECTION);
    }
}
