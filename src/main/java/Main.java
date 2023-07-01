import db.ConnectionPoolManager;
import view.View;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ConnectionPoolManager connectionPoolManager = ConnectionPoolManager.getInstance();

        try {
            Connection connection = connectionPoolManager.getConnection();
            new BaseBallApp(connection);
            connectionPoolManager.releaseConnection(connection);
        } catch (SQLException e) {
            View.printErrorMessage(e.getMessage() + "\n" + e.getCause());
        }

        try {
            connectionPoolManager.closeAllConnections();
        } catch (SQLException e) {
            View.printErrorMessage(e.getMessage() + "\n" + e.getCause());
        }
    }
}
