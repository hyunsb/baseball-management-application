package service;

import dao.StadiumDAO;
import db.DBConnection;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
import org.junit.jupiter.api.*;

import java.util.List;

class StadiumServiceTest {

    private static final StadiumService stadiumService
            = new StadiumService(new StadiumDAO(DBConnection.getInstance()));

    @AfterAll
    static void afterAll() {
        stadiumService.deleteAll();
    }

    @BeforeEach
    void setUp() {
        stadiumService.deleteAll();
    }

    @DisplayName("stadiumService 야구장 삽입 성공 테스트")
    @Test
    void save_Success_Test() {
        // Given
        String expected = "Test Stadium";
        StadiumRequest request = new StadiumRequest(expected);

        // When
        StadiumResponse actual = stadiumService.save(request);

        // Then
        Assertions.assertEquals(expected, actual.getName());
    }

    @DisplayName("stadiumService 야구장 삽입 실패 테스트")
    @Test
    void save_Failed_DuplicateName_Test() {
        // Given
        String expected = "Test Stadium";
        StadiumRequest request = new StadiumRequest(expected);
        stadiumService.save(request);

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            stadiumService.save(request);
        });
    }

    @DisplayName("stadiumService 전체 야구장 검색 테스트")
    @Test
    void findAll_Success_Test() {
        // Given
        StadiumRequest request1 = new StadiumRequest("Test Stadium1");
        stadiumService.save(request1);

        StadiumRequest request2 = new StadiumRequest("Test Stadium2");
        stadiumService.save(request2);

        // When
        List<StadiumResponse> actual = stadiumService.findAll();

        // Then
        Assertions.assertEquals(2, actual.size());
    }
}