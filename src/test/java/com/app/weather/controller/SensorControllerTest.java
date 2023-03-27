package com.app.weather.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.app.weather.models.SensorRequest;
import com.app.weather.models.SensorResponse;
import com.app.weather.processor.SensorProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SensorControllerTest {

    @InjectMocks
    private SensorController sensorController;
    @Mock
    private SensorProcessor processor;
    @Mock
    private SensorRequest sensorRequest;
    @Mock
    private SensorResponse sensorResponse;

    @Mock
    private List<SensorResponse> sensorResponseList;

    @Test
    public void shouldSaveSensorDataOk() {
        when(this.processor.save(any(SensorRequest.class))).thenReturn(this.sensorResponse);
        ResponseEntity<SensorResponse> result = this.sensorController.saveSensorData(this.sensorRequest);
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(this.sensorResponse, result.getBody());
    }

    @Test
    public void shouldFailWhenSensorDataCantSave() {
        when(this.processor.save(any(SensorRequest.class))).thenThrow(SdkClientException.class);
        Assertions.assertThrows(SdkClientException.class, () -> {
            this.sensorController.saveSensorData(this.sensorRequest);
        });
    }

    @Test
    public void shouldRetrieveBySensorId() {
        when(this.processor.getSensorDataList(any(String.class))).thenReturn(this.sensorResponseList);
        ResponseEntity<List<SensorResponse>> result = this.sensorController.getSensorById("100");
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(this.sensorResponseList, result.getBody());
    }

    @Test
    public void shouldRetrieveBySensorIdAndDates() {
        when(this.processor.getMetricsByDateRange(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(this.sensorResponse);
        ResponseEntity<SensorResponse> result = this.sensorController.getMetrics("100", LocalDate.of(2023, 3, 3), LocalDate.of(2023, 3, 30));
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(this.sensorResponse, result.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenCalled() {
        when(this.processor.getMetricsByDateRange(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenThrow(ResourceNotFoundException.class);
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> this.sensorController.getMetrics("100",
                        LocalDate.of(2023, 3, 3),
                        LocalDate.of(2023, 3, 30)));
    }

}
