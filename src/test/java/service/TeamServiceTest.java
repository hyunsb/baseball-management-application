package service;

import dao.TeamDAO;
import db.DBConnection;
import dto.team.TeamRequest;
import dto.team.TeamResponse;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.List;

class TeamServiceTest {

    private static final TeamService teamService;

    static {
        Connection connection = DBConnection.getInstance();
        TeamDAO teamDao = new TeamDAO(connection);

        teamService = new TeamService(teamDao);
    }

    @AfterAll
    static void afterAll() {
        teamService.deleteAll();
    }

    @BeforeEach
    void setUp() {
        teamService.deleteAll();
    }

    @DisplayName("teamService 팀 삽입 성공 테스트")
    @Test
    void save_Success_Test() {
        // Given
        String name = "test Team";
        Long stadiumId = 1L;
        TeamRequest.Create request = new TeamRequest.Create(name, stadiumId);

        // When
        TeamResponse actual = teamService.save(request);

        // Then
        Assertions.assertEquals(name, actual.getName());
        Assertions.assertEquals(stadiumId, actual.getStadiumId());
    }

    @DisplayName("teamService 팀 삽입 실패 테스트")
    @Test
    void save_Failed_DuplicateName_Test() {
        // Given
        String name = "test Team";
        Long stadiumId = 1L;
        TeamRequest.Create request = new TeamRequest.Create(name, stadiumId);
        teamService.save(request);

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> teamService.save(request));
    }

    @DisplayName("teamService 전체 팀 검색 테스트")
    @Test
    void findAll_Success_Test() {
        // Given
        String name1 = "test Team1";
        Long stadiumId1 = 1L;
        TeamRequest.Create request1 = new TeamRequest.Create(name1, stadiumId1);

        String name2 = "test Team2";
        Long stadiumId2 = 1L;
        TeamRequest.Create request2 = new TeamRequest.Create(name2, stadiumId2);

        teamService.save(request1);
        teamService.save(request2);

        // When
        List<TeamResponse> actual = teamService.findAll();

        // Then
        Assertions.assertEquals(2, actual.size());
    }
}