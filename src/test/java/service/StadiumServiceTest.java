package service;

import core.ConnectionPoolManager;
import dao.StadiumDAO;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class StadiumServiceTest {

    private static final ConnectionPoolManager CONNECTION_POOL_MANAGER = ConnectionPoolManager.getInstance();

    private static final StadiumDAO STADIUM_DAO = new StadiumDAO();
    private static final StadiumService STADIUM_SERVICE = new StadiumService(CONNECTION_POOL_MANAGER, STADIUM_DAO);

    @AfterAll
    static void afterAll() throws SQLException {
        Connection connection = CONNECTION_POOL_MANAGER.getConnection();
        STADIUM_DAO.deleteAll(connection);
        CONNECTION_POOL_MANAGER.releaseConnection(connection);
    }

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = CONNECTION_POOL_MANAGER.getConnection();
        STADIUM_DAO.deleteAll(connection);
        CONNECTION_POOL_MANAGER.releaseConnection(connection);
    }

    @DisplayName("stadiumService 야구장 삽입 성공 테스트")
    @Test
    void save_Success_Test() throws SQLException {
        // Given
        String expected = "Test Stadium";
        StadiumRequest request = new StadiumRequest(expected);

        // When
        StadiumResponse actual = STADIUM_SERVICE.save(request);

        // Then
        Assertions.assertEquals(expected, actual.getName());
    }

    @DisplayName("stadiumService 야구장 삽입 실패 테스트")
    @Test
    void save_Failed_DuplicateName_Test() throws SQLException {
        // Given
        String expected = "Test Stadium";
        StadiumRequest request = new StadiumRequest(expected);
        STADIUM_SERVICE.save(request);

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            STADIUM_SERVICE.save(request);
        });
    }

    @DisplayName("stadiumService 전체 야구장 검색 테스트")
    @Test
    void findAll_Success_Test() throws SQLException {
        // Given
        StadiumRequest request1 = new StadiumRequest("Test Stadium1");
        STADIUM_SERVICE.save(request1);

        StadiumRequest request2 = new StadiumRequest("Test Stadium2");
        STADIUM_SERVICE.save(request2);

        // When
        List<StadiumResponse> actual = STADIUM_SERVICE.findAll();

        // Then
        Assertions.assertEquals(2, actual.size());
    }
}