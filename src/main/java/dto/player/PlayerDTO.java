package dto.player;

import domain.Position;
import lombok.*;
import model.Player;

public class PlayerDTO {

    private PlayerDTO() {
        throw new IllegalStateException("Class for make Nested Dto");
    }

    @Getter
    @Builder
    public static class NewPlayerRequest {

        private final Long teamId;
        private final String name;
        private final Position position;
    }

    @Getter
    @Builder
    public static class FindPlayerResponse {

        private final Long id;
        private final String name;
        private final Position position;

        public static FindPlayerResponse from(Player player) {
            Position position = player.getPosition();
            return FindPlayerResponse.builder()
                    .id(player.getId())
                    .name(player.getName())
                    .position(position)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class FindPlayersByTeamRequest {

        private final Long teamId;
    }
}
