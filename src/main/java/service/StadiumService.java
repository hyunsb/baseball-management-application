package service;

import dao.StadiumDAO;
import dto.stadium.StadiumRequest;
import dto.stadium.StadiumResponse;
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
        Optional<Stadium> result;

        try {
            result = stadiumDAO.createStadium(name);
        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [insert]: " + exception.getMessage());
        }
        Stadium stadium = result.orElseThrow(() -> new IllegalArgumentException("Stadium save 실패"));

        return StadiumResponse.from(stadium);
    }

    // 전체 야구장 검색
    public List<StadiumResponse> findAll() {
        try {
            List<Stadium> stadiums = stadiumDAO.findAll();
            return stadiums.stream()
                    .map(StadiumResponse::from)
                    .collect(Collectors.toList());

        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [select All]: " + exception.getMessage());
        }
    }

    // 야구장 이름으로 검색
    public StadiumResponse findByName(StadiumRequest request) throws IllegalArgumentException {
        String name = request.getName();
        Optional<Stadium> result;

        try {
            result = stadiumDAO.findStadiumByName(name);
        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [select]: " + exception.getMessage());
        }
        Stadium stadium = result.orElseThrow(() -> new IllegalArgumentException(name + "에 해당하는 경기장이 존재하지 않습니다."));

        return StadiumResponse.from(stadium);
    }

    // 야구장 이름으로 삭제
    public void deleteByName(StadiumRequest request) {
        String name = request.getName();

        try {
            stadiumDAO.deleteByName(name);
        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [delete]: " + exception.getMessage());
        }
    }

    // 전체 야구장 삭제
    public void deleteAll() {
        try {
            stadiumDAO.deleteAll();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("SQL 에러 [delete All]: " + exception.getMessage());
        }
    }
}
