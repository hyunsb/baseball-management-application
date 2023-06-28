package dto.stadium;

import domain.Request;
import exception.BadRequestException;
import lombok.*;

import java.util.Map;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class StadiumRequest {

    private final String name;

    public static StadiumRequest from(Request request) throws BadRequestException {
        Map<String, String> body = request.getBody();
        validateBody(body);
        return new StadiumRequest(body.get("name"));
    }

    private static void validateBody(Map<String, String> body) throws BadRequestException {
        if (Objects.isNull(body)) throw new BadRequestException("요청에 필요한 데이터가 존재하지 않습니다.");
        if (body.size() != 2 || !body.containsKey("name")) throw new BadRequestException("올바르지 않은 데이터 형식 입니다.");
    }
}
