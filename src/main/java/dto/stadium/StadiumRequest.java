package dto.stadium;

import domain.Request;
import exception.BadRequestException;
import exception.ErrorMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
        if (Objects.isNull(body))
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_DATA);

        if (body.size() != 1 || !body.containsKey("name"))
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_FORMAT);
    }
}
