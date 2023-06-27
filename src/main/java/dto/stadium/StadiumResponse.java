package dto.stadium;

import lombok.*;
import model.Stadium;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StadiumResponse {

    private Long id;
    private String name;
    private Timestamp createAt;

    public static StadiumResponse from(Stadium stadium) {
        return StadiumResponse.builder()
                .id(stadium.getId())
                .name(stadium.getName())
                .createAt(stadium.getCreatedAt())
                .build();
    }
}