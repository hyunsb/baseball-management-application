package dto.stadium;

import lombok.*;
import model.Stadium;

import java.sql.Timestamp;

@Getter
@Builder
public class StadiumResponseDTO {

    private Long id;
    private String name;
    private Timestamp createAt;

    public static StadiumResponseDTO from(Stadium stadium) {
        return StadiumResponseDTO.builder()
                .id(stadium.getId())
                .name(stadium.getName())
                .createAt(stadium.getCreatedAt())
                .build();
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", NAME: " + name +
                ", 생성일자: " + createAt;
    }
}
