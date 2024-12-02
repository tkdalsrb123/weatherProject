package zerobase.weatherproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

//    @Value("${openweathermap.key}")
//    private String API_KEY;
//
//    @Test
//    void getWeatherAPI_SUCCESS() {
//        //given
//        DiaryService diaryService = new DiaryService();
//        ReflectionTestUtils.setField(diaryService, "API_KEY", API_KEY);
//        //when
//        String response = diaryService.getWeatherAPI();
//        //then
//        assertNotNull(response);
//    }

}