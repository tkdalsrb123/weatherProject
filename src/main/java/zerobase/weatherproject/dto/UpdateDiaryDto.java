package zerobase.weatherproject.dto;


import lombok.*;

import java.time.LocalDate;


public class UpdateDiaryDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String text;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String preText;
        private String postText;
        private LocalDate date;

        public static Response from(String preText, DiaryDto diary) {
            return Response.builder()
                    .preText(preText)
                    .postText(diary.getText())
                    .date(diary.getDate())
                    .build();
        }
    }

}
