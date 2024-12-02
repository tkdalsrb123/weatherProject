package zerobase.weatherproject.controller;


import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weatherproject.dto.CreateDiaryDto;
import zerobase.weatherproject.dto.DiaryDto;
import zerobase.weatherproject.dto.UpdateDiaryDto;
import zerobase.weatherproject.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/create/diary")
    CreateDiaryDto.Response createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody CreateDiaryDto.Request request) {
        return CreateDiaryDto.Response.from(diaryService.createDiary(date, request.getText()));
    }

    @GetMapping("/read/diary")
    List<DiaryDto> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }

    @GetMapping("/read/diaries")
    List<DiaryDto> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    UpdateDiaryDto.Response updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody UpdateDiaryDto.Request request) {
        return diaryService.updateDiary(date, request.getText());
    }

    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }

}
