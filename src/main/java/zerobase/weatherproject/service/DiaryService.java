package zerobase.weatherproject.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weatherproject.domain.DateWeather;
import zerobase.weatherproject.domain.Diary;
import zerobase.weatherproject.dto.DiaryDto;
import zerobase.weatherproject.dto.UpdateDiaryDto;
import zerobase.weatherproject.repository.DateWeatherRepository;
import zerobase.weatherproject.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiaryService {

    @Value("${openweathermap.key}")
    private String API_KEY;

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi());
    }

    @Transactional
    public DiaryDto createDiary(LocalDate date, String text) {
        DateWeather dateWeather = getDateWeather(date);
        Diary diary = diaryRepository.save(Diary.builder()
                        .weather(dateWeather.getWeather())
                        .icon(dateWeather.getIcon())
                        .temperature(dateWeather.getTemperature())
                        .text(text)
                        .date(date)
                        .build());

        return DiaryDto.fromEntity(diary);
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherList = dateWeatherRepository.findAllByDate(date);

        if (dateWeatherList.size() == 0) {
            return getWeatherFromApi();
        } else {
            return dateWeatherList.get(0);
        }

    }

    private DateWeather getWeatherFromApi() {
        String weatherData = getWeatherString();

        Map<String, Object> parseWeather = parseWeather(weatherData);
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parseWeather.get("main").toString());
        dateWeather.setIcon(parseWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parseWeather.get("temp"));
        return dateWeather;
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + API_KEY;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    public Map<String, Object> parseWeather(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject main = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", main.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherObject = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherObject.get("main"));
        resultMap.put("icon", weatherObject.get("icon"));

        return resultMap;
    }

    @Transactional(readOnly = true)
    public List<DiaryDto> readDiary(LocalDate date) {
        List<Diary> diaryList =  diaryRepository.findAllByDate(date);

        return diaryList.stream()
                .map(diary -> DiaryDto.fromEntity(diary))
                .collect(Collectors.toList());
    }

    public List<DiaryDto> readDiaries(LocalDate startDate, LocalDate endDate) {
        List<Diary> diaryList = diaryRepository.findAllByDateBetween(startDate, endDate);

        return diaryList.stream()
                .map(diary -> DiaryDto.fromEntity(diary))
                .collect(Collectors.toList());
    }

    public UpdateDiaryDto.Response updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        String preText = nowDiary.getText();
        nowDiary.setText(text);
        DiaryDto diary = DiaryDto.fromEntity(diaryRepository.save(nowDiary));
        return UpdateDiaryDto.Response.from(preText, diary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
