package dto.player;

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
        private final String position;
    }

    @Getter
    @Builder
    public static class FindPlayerResponse {

        private final Long id;
        private final String name;
        private final String position;

        public static FindPlayerResponse from(Player player) {
            return FindPlayerResponse.builder()
                    .id(player.getId())
                    .name(player.getName())
                    .position(player.getPosition().getValue())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class FindPlayersByTeamRequest {

        private final Long teamId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class UpdatePlayerTeamIdForOutRequest {

        private final Long id;
    }
}
