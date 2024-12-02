package zerobase.weatherproject.doamain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Diary {
    @Id
    @GeneratedValue
    private Long id;

    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

}
