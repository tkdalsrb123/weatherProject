package zerobase.weatherproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.weatherproject.dto.CreateDiaryDto;
import zerobase.weatherproject.dto.DiaryDto;
import zerobase.weatherproject.service.DiaryService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successCreateDiary() throws Exception {
        //given
        given(diaryService.createDiary(any(LocalDate.class), anyString()))
                .willReturn(DiaryDto.builder()
                        .date(LocalDate.now())
                        .icon("abc")
                        .text("good")
                        .temperature(0.0)
                        .weather("rain")
                        .build());
        //when
        //then
        mockMvc.perform(post("/create/diary")
                        .param("date", "2024-11-30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateDiaryDto.Request("good"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("good"))
                .andDo(print());

    }

}