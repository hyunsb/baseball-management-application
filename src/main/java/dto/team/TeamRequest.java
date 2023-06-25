package dto.team;

import lombok.*;

public class TeamRequest {

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Create {

        private String name;
        private Long stadiumId;
    }
}
