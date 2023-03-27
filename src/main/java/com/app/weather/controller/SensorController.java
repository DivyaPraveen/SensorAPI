package com.app.weather.controller;

import com.app.weather.models.SensorRequest;
import com.app.weather.models.SensorResponse;
import com.app.weather.processor.SensorProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v0")
@Slf4j
public class SensorController {
    private SensorProcessor processor;

    @Autowired
    public void setProcessor(SensorProcessor processor) {
        this.processor = processor;
    }

    @SneakyThrows
    @PostMapping("/sensor")
    public ResponseEntity<SensorResponse> saveSensorData(@RequestBody SensorRequest sensorRequest) {
        return new ResponseEntity<>(processor.save(sensorRequest), HttpStatus.CREATED);
    }

    @SneakyThrows
    @GetMapping("/id")
    public ResponseEntity<List<SensorResponse>> getSensorById(
            @RequestParam(name = "sensorId")
            String sensorId) {
        log.debug("Sensor id: " + sensorId);
        return ResponseEntity.ok(processor.getSensorDataList(sensorId));
    }

    @SneakyThrows
    @GetMapping("/{sensorId}/date")
    public ResponseEntity<SensorResponse> getMetrics(@PathVariable("sensorId") String sensorId,
                                                     @RequestParam(value = "dateFrom")
                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
                                                     @RequestParam(value = "dateTo")
                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo) {
        log.debug("Sensor id: " + sensorId);
        return ResponseEntity.ok(processor.getMetricsByDateRange(sensorId, dateFrom, dateTo));
    }
}
