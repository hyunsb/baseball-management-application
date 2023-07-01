package db;

import exception.DBConnectException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConnectionPoolManager {

    private static final int MAX_POOL_SIZE = 10;

    private static ConnectionPoolManager connectionPoolManager;

    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections;

    private ConnectionPoolManager() {
        connectionPool = new ArrayList<>();
        usedConnections = new ArrayList<>();
    }

    public static ConnectionPoolManager getInstance() {
        if (Objects.isNull(connectionPoolManager)) {
            return new ConnectionPoolManager();
        }
        return connectionPoolManager;
    }

    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty() && usedConnections.size() < MAX_POOL_SIZE) {
            connectionPool.add(createConnection());

            final Connection connection = connectionPool.remove(connectionPool.size() - 1);
            usedConnections.add(connection);
            return connection;
        }
        throw new SQLException("커넥션 풀이 가득 찼습니다, 새로운 커넥션을 만들 수 없습니다.");
    }

    private Connection createConnection() throws DBConnectException {
        return DBConnection.getInstance(DBConnectConfig.URL.value(),
                DBConnectConfig.USER_NAME.value(),
                DBConnectConfig.PASSWORD.value(),
                DBConnectConfig.DRIVER.value());
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
