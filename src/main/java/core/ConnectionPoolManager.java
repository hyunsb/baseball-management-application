package core;

import db.DBConnectConfig;
import db.DBConnection;
import exception.DBConnectException;
import exception.ErrorMessage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConnectionPoolManager {

    private static final int INIT_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;

    private static ConnectionPoolManager connectionPoolManager;

    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections;

    private ConnectionPoolManager() {
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < INIT_POOL_SIZE; i++) {
            connections.add(createConnection());
        }

        connectionPool = connections;
        usedConnections = new ArrayList<>();
    }

    public static ConnectionPoolManager getInstance() {
        if (Objects.isNull(connectionPoolManager)) {
            return new ConnectionPoolManager();
        }
        return connectionPoolManager;
    }

    public Connection getConnection() throws DBConnectException {
        if (connectionPool.isEmpty() && usedConnections.size() == MAX_POOL_SIZE)
            throw new DBConnectException(ErrorMessage.CONNECTION_POOL_IS_FULL);

        if (connectionPool.isEmpty() && usedConnections.size() < MAX_POOL_SIZE) {
            connectionPool.add(createConnection());
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        try {
            if (!connection.isValid(5)) {
                connection.close();
                connection = updateConnection();
            }
        } catch (SQLException exception) {
            throw new DBConnectException(ErrorMessage.INVALID_DB_CONNECTION);
        }
        usedConnections.add(connection);
        return connection;
    }

    public void printStatus() {
        System.out.println("connectionPool size:" + connectionPool.size());
        System.out.println("usedConnectionPool size:" + usedConnections.size());
    }

    private Connection createConnection() throws DBConnectException {
        return DBConnection.getInstance(DBConnectConfig.URL.value(),
                DBConnectConfig.USER_NAME.value(),
                DBConnectConfig.PASSWORD.value(),
                DBConnectConfig.DRIVER.value());
    }

    private Connection updateConnection() throws DBConnectException {
        return createConnection();
    }

    public boolean releaseConnection(Connection connection) {
        if (usedConnections.remove(connection)) {
            connectionPool.add(connection);
            return true;
        }
        return false;
    }

    public void closeAllConnections() throws SQLException {
        for (Connection connection : connectionPool) connection.close();
        for (Connection connection : usedConnections) connection.close();
    }
}
