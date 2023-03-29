package com.app.weather.processor.util;

import com.app.weather.models.SensorResponse;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

/**
 * Utility class of SensorProcessor
 */
public class SensorProcessorUtil {

    protected SensorResponse findMetrics(@NotEmpty @NotNull List<SensorResponse> sensorResponseList) {
        SensorResponse newSensorResponse = new SensorResponse();
        boolean present = sensorResponseList.stream().findFirst().isPresent();
        newSensorResponse.setSensorId(present ? sensorResponseList.stream().findFirst().get().getSensorId() : "");
        setTemperatureMetrics(sensorResponseList, newSensorResponse);
        setHumidityMetrics(sensorResponseList, newSensorResponse);
        return newSensorResponse;
    }

    private void setTemperatureMetrics(List<SensorResponse> sensorResponseList, SensorResponse newSensorResponse) {
        OptionalDouble temperatureAverage = sensorResponseList.stream().mapToInt(SensorResponse::getTempData).average();
        OptionalInt temperatureMin = sensorResponseList.stream().mapToInt(SensorResponse::getTempData).min();
        OptionalInt temperatureMax = sensorResponseList.stream().mapToInt(SensorResponse::getTempData).max();

        if (temperatureAverage.isPresent())
            newSensorResponse.setTempAvg( temperatureAverage.getAsDouble());
        if (temperatureMin.isPresent())
            newSensorResponse.setTempMin(temperatureMin.getAsInt());
        if (temperatureMax.isPresent())
            newSensorResponse.setTempMax(temperatureMax.getAsInt());
    }

    private void setHumidityMetrics(List<SensorResponse> sensorResponseList, SensorResponse newSensorResponse) {

        OptionalDouble humidityAverage = sensorResponseList.stream().mapToInt(SensorResponse::getHumidityData).average();
        OptionalInt humidityMin = sensorResponseList.stream().mapToInt(SensorResponse::getHumidityData).min();
        OptionalInt humidityMax = sensorResponseList.stream().mapToInt(SensorResponse::getHumidityData).max();

        if (humidityAverage.isPresent())
            newSensorResponse.setHumidityAvg(humidityAverage.getAsDouble());
        if (humidityMin.isPresent())
            newSensorResponse.setHumidityMin(humidityMin.getAsInt());
        if (humidityMax.isPresent())
            newSensorResponse.setHumidityMax(humidityMax.getAsInt());
    }

}
