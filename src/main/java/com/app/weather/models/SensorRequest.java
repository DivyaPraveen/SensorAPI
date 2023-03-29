package com.app.weather.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SensorRequest {

    private String sensorId;
    private String tempData;
    private String humidityData;


}
