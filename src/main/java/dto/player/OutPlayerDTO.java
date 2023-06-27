package dto.player;

import lombok.Builder;
import lombok.Getter;
import model.OutPlayer;
import java.sql.Timestamp;

public class OutPlayerDTO {

    private OutPlayerDTO() {
        throw new IllegalStateException("Class for make Nested Dto");
    }

    @Getter
    @Builder
    public static class NewOutPlayerRequest {

        private final Long playerId;
        private final String reason;
    }

    @Getter
    @Builder
    public static class FindOutPlayerResponse {

        private final Long playerId;
        private final String name;
        private final String position;
        private final String reason;
        private final Timestamp outDay;

        public static FindOutPlayerResponse from(OutPlayer outPlayer) {
            return FindOutPlayerResponse.builder()
                    .playerId(outPlayer.getPlayerId())
                    .name(outPlayer.getName())
                    .position(outPlayer.getPosition().getValue())
                    .reason(outPlayer.getReason().getValue())
                    .outDay(outPlayer.getOutDay())
                    .build();
        }
    }
}
