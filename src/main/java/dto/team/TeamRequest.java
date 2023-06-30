package dto.team;

import domain.Request;
import exception.BadRequestException;
import lombok.*;

import java.util.Map;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class TeamRequest {

    private final String name;
    private final Long stadiumId;

    public static TeamRequest from(Request request) throws BadRequestException {
        Map<String, String> body = request.getBody();
        validateBody(body);

        String name = body.get("name");
        Long stadiumId = convertStadiumIdToLong(body.get("stadiumId"));

        return new TeamRequest(name, stadiumId);
    }

    private static void validateBody(Map<String, String> body) throws BadRequestException {
        if (Objects.isNull(body))
            throw new BadRequestException("요청에 필요한 데이터가 존재하지 않습니다.");

        if (body.size() != 2 || !body.containsKey("name") || !body.containsKey("stadiumId"))
            throw new BadRequestException("올바르지 않은 데이터 형식 입니다.");
    }

    private static Long convertStadiumIdToLong(String stadiumId) throws BadRequestException {
        try {
            return Long.parseLong(stadiumId);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("올바르지 않은 데이터 형식 입니다.");
        }
    }
}
