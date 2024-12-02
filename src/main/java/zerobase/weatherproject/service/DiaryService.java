package zerobase.weatherproject.service;


import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weatherproject.doamain.Diary;
import zerobase.weatherproject.dto.CreateDiaryDto;
import zerobase.weatherproject.dto.DiaryDto;
import zerobase.weatherproject.dto.UpdateDiaryDto;
import zerobase.weatherproject.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    @Value("${openweathermap.key}")
    private String API_KEY;

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Transactional
    public DiaryDto createDiary(LocalDate date, String text) {
//        Diary newDiary = new Diary();

        Map<String, Object> mapWeather = parseWeather(getWeatherAPI());
//        newDiary.setTemperature((Double) mapWeather.get("temp"));
//        newDiary.setIcon(mapWeather.get("icon").toString());
//        newDiary.setWeather(mapWeather.get("main").toString());
//        newDiary.setText(text);
//        newDiary.setDate(date);

        Diary diary = diaryRepository.save(Diary.builder()
                .weather(mapWeather.get("main").toString())
                .icon(mapWeather.get("icon").toString())
                .temperature((Double) mapWeather.get("temp"))
                .text(text)
                .date(date)
                .build());

        return DiaryDto.fromEntity(diary);
    }


    private String getWeatherAPI() {
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
