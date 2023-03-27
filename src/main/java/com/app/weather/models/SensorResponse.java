package com.app.weather.models;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data @Component
public class SensorResponse {

    private String sensorId;
    private int tempData;
    private int tempMin;
    private int tempMax;
    private Double tempAvg;

    private int humidityData;
    private int humidityMax;
    private int humidityMin;
    private Double humidityAvg;

    private int windSpeed;

    private LocalDate dateFrom;
    private LocalDate dateTo;
}
