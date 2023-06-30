package service;

import dao.StadiumDAO;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
import exception.StadiumFindFailureException;
import exception.StadiumRegistrationFailureException;
import model.Stadium;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StadiumService {

    private final StadiumDAO stadiumDAO;

    public StadiumService(StadiumDAO stadiumDAO) {
        this.stadiumDAO = stadiumDAO;
    }

    // 야구장 저장
    public StadiumResponse save(StadiumRequest request) throws IllegalArgumentException {
        String name = request.getName();
        Optional<Stadium> stadium;

        try {
            stadium = stadiumDAO.createStadium(name);
        } catch (SQLException exception) {
            throw new StadiumRegistrationFailureException(exception.getMessage());
        }
        return StadiumResponse.from(stadium.orElseThrow(() ->
                new StadiumRegistrationFailureException("야구장이 존재하지 않습니다.")));
    }

    // 전체 야구장 검색
    public List<StadiumResponse> findAll() throws StadiumFindFailureException {
        try {
            List<Stadium> stadiums = stadiumDAO.findAll();
            return stadiums.stream()
                    .map(StadiumResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new StadiumFindFailureException(exception.getMessage());
        }
    }

    // 야구장 이름으로 검색
    public StadiumResponse findByName(StadiumRequest request) throws StadiumFindFailureException {
        String name = request.getName();
        Optional<Stadium> result;

        try {
            result = stadiumDAO.findStadiumByName(name);
        } catch (SQLException exception) {
            throw new StadiumFindFailureException(exception.getMessage());
        }

        return StadiumResponse.from(result.orElseThrow(() ->
                new StadiumFindFailureException(name + "에 해당하는 경기장이 존재하지 않습니다.")));
    }
}
