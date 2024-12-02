package zerobase.weatherproject.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.weatherproject.dto.CreateDiaryDto;
import zerobase.weatherproject.service.DiaryService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary/create")
    public CreateDiaryDto.Response createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody CreateDiaryDto.Request request) {
        System.out.println(request);
        return CreateDiaryDto.Response.from(diaryService.createDiary(date, request.getText()));
    }
}
