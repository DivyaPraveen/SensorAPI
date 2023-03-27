package com.app.weather.repository;

import com.app.weather.models.SensorDataInfo;

import java.time.LocalDate;
import java.util.List;

public interface ISensorRepo {

    SensorDataInfo saveSensorData(SensorDataInfo sensorDataInfo);

    List<SensorDataInfo> findSensorDataById(String sensorId);

    List<SensorDataInfo> findSensorIdBetween(String sensorId, LocalDate dateFrom, LocalDate dateTo);
}
