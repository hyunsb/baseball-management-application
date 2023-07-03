package service;

import core.ConnectionPoolManager;
import dao.StadiumDAO;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
import exception.ErrorMessage;
import exception.StadiumFindFailureException;
import exception.StadiumRegistrationFailureException;
import lombok.RequiredArgsConstructor;
import model.Stadium;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StadiumService {

    private final ConnectionPoolManager connectionPoolManager;
    private final StadiumDAO stadiumDAO;

    // 야구장 저장
    public StadiumResponse save(StadiumRequest request) throws IllegalArgumentException {
        Connection connection = connectionPoolManager.getConnection();
        String name = request.getName();
        Optional<Stadium> stadium;

        try {
            stadium = stadiumDAO.createStadium(connection, name);
        } catch (SQLException exception) {
            throw new StadiumRegistrationFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
        return StadiumResponse.from(stadium.orElseThrow(() ->
                new StadiumRegistrationFailureException(ErrorMessage.FAILED_STADIUM_FIND)));
    }

    // 전체 야구장 검색
    public List<StadiumResponse> findAll() throws StadiumFindFailureException {
        Connection connection = connectionPoolManager.getConnection();
        try {
            List<Stadium> stadiums = stadiumDAO.findAll(connection);
            return stadiums.stream()
                    .map(StadiumResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new StadiumFindFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }
    }

    // 야구장 이름으로 검색
    public StadiumResponse findByName(StadiumRequest request) throws StadiumFindFailureException {
        Connection connection = connectionPoolManager.getConnection();
        String name = request.getName();
        Optional<Stadium> result;

        try {
            result = stadiumDAO.findStadiumByName(connection, name);
        } catch (SQLException exception) {
            throw new StadiumFindFailureException(exception.getMessage());
        } finally {
            connectionPoolManager.releaseConnection(connection);
        }

        return StadiumResponse.from(result.orElseThrow(() ->
                new StadiumFindFailureException(ErrorMessage.FAILED_STADIUM_FIND)));
    }
}
