package dto.player;

import domain.Request;
import exception.BadRequestException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.OutPlayer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OutPlayerDTO {

    private OutPlayerDTO() {
        throw new IllegalStateException("Class for make Nested Dto");
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class NewOutPlayerRequest {

        private final Long playerId;
        private final String reason;

        public static NewOutPlayerRequest from(Request request) throws BadRequestException {
            Map<String, String> body = request.getBody();
            List<String> keys = new ArrayList<>();
            keys.add("playerId");
            keys.add("reason");
            validateBody(body, keys);

            Long playerId = convertStringToLong(body.get("playerId"));
            String reason = body.get("reason");
            return new NewOutPlayerRequest(playerId, reason);
        }
    }



    private static void validateBody(Map<String, String> body, List<String> keys) throws BadRequestException {
        if (Objects.isNull(body))
            throw new BadRequestException("요청에 필요한 데이터가 존재하지 않습니다.");

        if (body.size() != 2) {
            for (String key : keys) {
                if(!body.containsKey(key)){
                    throw new BadRequestException("올바르지 않은 데이터 형식 입니다.");
                }
            }
        }
    }

    private static Long convertStringToLong(String playerId) throws BadRequestException {
        try {
            return Long.parseLong(playerId);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("올바르지 않은 데이터 형식 입니다.");
        }
    }

    @Getter
    @Builder
    public static class FindOutPlayerResponse {

        private final Long playerId;
        private final String name;
        private final String position;
        @Setter
        private String reason;
        @Setter
        private Timestamp outDay;

        public static FindOutPlayerResponse from(OutPlayer outPlayer) {
            FindOutPlayerResponseBuilder findOutPlayerResponseBuilder = FindOutPlayerResponse.builder()
                    .playerId(outPlayer.getPlayerId())
                    .name(outPlayer.getName())
                    .position(outPlayer.getPosition().getValue());

            try {
                if (outPlayer.getReason().getValue() != null) {
                    findOutPlayerResponseBuilder
                            .reason(outPlayer.getReason().getValue())
                            .outDay(outPlayer.getOutDay());
                }

            } catch (Exception exception) {

            }
            return findOutPlayerResponseBuilder.build();
        }
    }
}
