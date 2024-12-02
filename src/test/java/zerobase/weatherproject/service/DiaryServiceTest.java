package zerobase.weatherproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import zerobase.weatherproject.doamain.Diary;
import zerobase.weatherproject.dto.DiaryDto;
import zerobase.weatherproject.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {


    @InjectMocks
    private DiaryService diaryService; // 테스트 대상 서비스

    @Mock
    private DiaryRepository diaryRepository; // Repository Mock

    @Mock
    private Map<String, Object> weatherMock; // API 호출 Mock

//    public DiaryServiceTest() {
//        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
//    }

    @Test
    void createDiarySuccess() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 11, 30);
        String testText = "Test diary entry";
        Map<String, Object> mockWeather = new HashMap<>();
        mockWeather.put("main", "Clear");
        mockWeather.put("icon", "01d");
        mockWeather.put("temp", 25.0);

        // Mock 외부 의존성
        given(diaryService.parseWeather(anyString())).willReturn(mockWeather); // API 호출 Mock
        given(diaryRepository.save(any(Diary.class))).willAnswer(invocation -> {
            Diary diary = invocation.getArgument(0);
            diary.setId(1L);
            return diary;
        });

        // When
        DiaryDto result = diaryService.createDiary(testDate, testText);

        // Then
        assertThat(result.getDate()).isEqualTo(testDate);
        assertThat(result.getText()).isEqualTo(testText);
        assertThat(result.getWeather()).isEqualTo("Clear");
        assertThat(result.getTemperature()).isEqualTo(25.0);
    }

}