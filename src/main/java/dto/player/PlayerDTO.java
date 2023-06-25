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
    public static class Request {

        private final Long teamId;
        private final String name;
        private final Position position;

        public Player toEntity(){
            return Player.builder()
                    .teamId(teamId)
                    .name(name)
                    .position(position)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Response {

        private final Long id;
        private final String name;
        private final Position position;

        public static Response from(Player player) {
            Position position = player.getPosition();
            return Response.builder()
                    .id(player.getId())
                    .name(player.getName())
                    .position(position)
                    .build();
        }
    }
}
