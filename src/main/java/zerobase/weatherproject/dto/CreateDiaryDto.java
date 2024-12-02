package zerobase.weatherproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;


public class CreateDiaryDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotEmpty
        private String text;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String text;
        private LocalDate date;
        private String weather;

        public static Response from(DiaryDto diary) {
            return Response.builder()
                    .text(diary.getText())
                    .date(diary.getDate())
                    .weather(diary.getWeather())
                    .build();
        }

    }


}
