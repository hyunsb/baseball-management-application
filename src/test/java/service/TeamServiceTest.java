package service;

import core.ConnectionPoolManager;
import dao.StadiumDAO;
import dao.TeamDAO;
import dto.stadium.StadiumRequestDTO;
import dto.team.TeamRequestDTO;
import dto.team.TeamResponseDTO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class TeamServiceTest {

    private static final ConnectionPoolManager CONNECTION_POOL_MANAGER = ConnectionPoolManager.getInstance();
    private static final TeamDAO TEAM_DAO = new TeamDAO();
    private static final StadiumDAO STADIUM_DAO = new StadiumDAO();
    private static final TeamService TEAM_SERVICE = new TeamService(CONNECTION_POOL_MANAGER, TEAM_DAO, STADIUM_DAO);
    private static final StadiumService STADIUM_SERVICE = new StadiumService(CONNECTION_POOL_MANAGER, STADIUM_DAO);

    @AfterAll
    static void afterAll() throws SQLException {
        Connection connection = CONNECTION_POOL_MANAGER.getConnection();
        TEAM_DAO.deleteAll(connection);
        STADIUM_DAO.deleteAll(connection);
        CONNECTION_POOL_MANAGER.releaseConnection(connection);
    }

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = CONNECTION_POOL_MANAGER.getConnection();
        TEAM_DAO.deleteAll(connection);
        STADIUM_DAO.deleteAll(connection);
        CONNECTION_POOL_MANAGER.releaseConnection(connection);
    }

    @DisplayName("teamService 팀 삽입 성공 테스트")
    @Test
    void save_Success_Test() throws SQLException {
        // Given
        STADIUM_SERVICE.save(new StadiumRequestDTO("Test Stadium"));

        String name = "test Team";
        Long stadiumId = 1L;
        TeamRequestDTO request = new TeamRequestDTO(name, stadiumId);

        // When
        TeamResponseDTO actual = TEAM_SERVICE.save(request);

        // Then
        Assertions.assertEquals(name, actual.getName());
        Assertions.assertEquals(stadiumId, actual.getStadiumId());
    }

    @DisplayName("teamService 팀 삽입 실패 테스트")
    @Test
    void save_Failed_DuplicateName_Test() throws SQLException {
        // Given
        String name = "test Team";
        Long stadiumId = 1L;
        TeamRequestDTO request = new TeamRequestDTO(name, stadiumId);
        TEAM_SERVICE.save(request);

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> TEAM_SERVICE.save(request));
    }

    @DisplayName("teamService 전체 팀 검색 테스트")
    @Test
    void findAll_Success_Test() throws SQLException {
        // Given
        String name1 = "test Team1";
        Long stadiumId1 = 1L;
        TeamRequestDTO request1 = new TeamRequestDTO(name1, stadiumId1);

        String name2 = "test Team2";
        Long stadiumId2 = 1L;
        TeamRequestDTO request2 = new TeamRequestDTO(name2, stadiumId2);

        TEAM_SERVICE.save(request1);
        TEAM_SERVICE.save(request2);

        // When
        List<TeamResponseDTO> actual = TEAM_SERVICE.findAll();

        // Then
        Assertions.assertEquals(2, actual.size());
    }
}