package zerobase.weatherproject.dto;


import lombok.*;
import zerobase.weatherproject.domain.Diary;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryDto {
    private LocalDate date;
    private String text;
    private String weather;
    private Double temperature;
    private String icon;

    public static DiaryDto fromEntity(Diary diary) {
        return DiaryDto.builder()
                .date(diary.getDate())
                .text(diary.getText())
                .weather(diary.getWeather())
                .temperature(diary.getTemperature())
                .icon(diary.getIcon())
                .build();
    }
}
