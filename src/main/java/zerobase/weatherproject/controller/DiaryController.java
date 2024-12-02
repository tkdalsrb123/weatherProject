package zerobase.weatherproject.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary="get create diary data", description = "다이어리 생성")
    @PostMapping("/create/diary")
    CreateDiaryDto.Response createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "기간을 작성하시오", example = "2024-12-02") LocalDate date, @RequestBody CreateDiaryDto.Request request) {
        return CreateDiaryDto.Response.from(diaryService.createDiary(date, request.getText()));
    }

    @Operation(summary="get diary data from date", description = "해당 날짜의 다이어리 가져오기")
    @GetMapping("/read/diary")
    List<DiaryDto> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }


    @Operation(summary="get diaries data from date", description = "날짜 범위 안에 다이어리 가져오기")
    @GetMapping("/read/diaries")
    List<DiaryDto> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary="update first diary data for a specific date", description = "해당 날짜의 첫번째 다이어리 내용 수정")
    @PutMapping("/update/diary")
    UpdateDiaryDto.Response updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody UpdateDiaryDto.Request request) {
        return diaryService.updateDiary(date, request.getText());
    }

    @Operation(summary="Delete all diary entries for a specific date", description = "해당 날짜의 다이어리 데이터 전부 삭제")
    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }

}
