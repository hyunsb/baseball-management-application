package dto.team;

import domain.Request;
import exception.BadRequestException;
import exception.ErrorMessage;
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
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_DATA);

        if (body.size() != 2 || !body.containsKey("name") || !body.containsKey("stadiumId"))
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_FORMAT);
    }

    private static Long convertStadiumIdToLong(String stadiumId) throws BadRequestException {
        try {
            return Long.parseLong(stadiumId);
        } catch (NumberFormatException exception) {
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_DATA);
        }
    }
}
