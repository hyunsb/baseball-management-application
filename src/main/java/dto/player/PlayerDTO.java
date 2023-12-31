package dto.player;

import domain.Request;
import exception.BadRequestException;
import exception.ErrorMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerDTO {

    private PlayerDTO() {
        throw new IllegalStateException("Class for make Nested Dto");
    }

    private static void validateBody(Map<String, String> body, List<String> keys) throws BadRequestException {
        if (Objects.isNull(body))
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_DATA);

        if (body.size() != 2) {
            for (String key : keys) {
                if (!body.containsKey(key)) {
                    throw new BadRequestException(ErrorMessage.INVALID_REQUEST_FORMAT);
                }
            }
        }
    }

    private static Long convertStringToLong(String teamId) throws BadRequestException {
        try {
            return Long.parseLong(teamId);
        } catch (NumberFormatException exception) {
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_FORMAT);
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class NewPlayerRequest {

        private final Long teamId;
        private final String name;
        private final String position;

        public static PlayerDTO.NewPlayerRequest from(Request request) throws BadRequestException {
            Map<String, String> body = request.getBody();

            List<String> keys = new ArrayList<>();
            keys.add("name");
            keys.add("position");
            keys.add("teamId");

            validateBody(body, keys);
            Long teamId = convertStringToLong(body.get("teamId"));
            String name = body.get("name");
            String position = body.get("position");

            return new PlayerDTO.NewPlayerRequest(teamId, name, position);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class FindPlayersByTeamRequest {

        private final Long teamId;

        public static PlayerDTO.FindPlayersByTeamRequest from(Request request) throws BadRequestException {
            Map<String, String> body = request.getBody();

            List<String> keys = new ArrayList<>();
            keys.add("teamId");

            validateBody(body, keys);
            Long teamId = convertStringToLong(body.get("teamId"));

            return new PlayerDTO.FindPlayersByTeamRequest(teamId);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class UpdatePlayerTeamIdForOutRequest {

        private final Long id;

        public static PlayerDTO.UpdatePlayerTeamIdForOutRequest from(Request request) throws BadRequestException {
            Map<String, String> body = request.getBody();

            List<String> keys = new ArrayList<>();
            keys.add("id");

            validateBody(body, keys);
            Long id = convertStringToLong(body.get("id"));

            return new PlayerDTO.UpdatePlayerTeamIdForOutRequest(id);
        }
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
    @Builder
    public static class FindPlayerGroupByPositionResponse {

        private final String name;
        private final String position;
        private final String teamName;

        public static FindPlayerGroupByPositionResponse from(Player player) {
            return FindPlayerGroupByPositionResponse.builder()
                    .name(player.getName())
                    .position(player.getPosition().getValue())
                    .teamName(player.getTeamName())
                    .build();
        }
    }
}
