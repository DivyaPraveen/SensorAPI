package com.app.weather.processor;

import com.app.weather.mapper.SensorDTOMapper;
import com.app.weather.models.SensorDataInfo;
import com.app.weather.models.SensorRequest;
import com.app.weather.models.SensorResponse;
import com.app.weather.repository.ISensorRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SensorProcessorTest {

    @InjectMocks
    private SensorProcessor sensorProcessor;
    @Mock
    private SensorDTOMapper sensorDTOMapper;
    @Mock
    private ISensorRepo iSensorRepo;
    @Mock
    private SensorDataInfo sensorDataInfo;

    private SensorRequest setUpSensorRequest() {
        SensorRequest sensorRequest = new SensorRequest();
        sensorRequest.setSensorId("100");
        sensorRequest.setTempData("32");
        sensorRequest.setHumidityData("80");
        return sensorRequest;
    }

    private SensorDataInfo sensorDataEntity(String sensorId, String tempData, String humidityDate, String createts) {
        SensorDataInfo sensorDataInfo = new SensorDataInfo();
        sensorDataInfo.setSensorId(sensorId);
        sensorDataInfo.setTempData(tempData);
        sensorDataInfo.setHumidityData(humidityDate);
        sensorDataInfo.setCreateTs(LocalDate.parse(createts));
        return sensorDataInfo;
    }

    private SensorResponse sensorResponse(String sensorId, int tempData, int humidityDate) {
        SensorResponse sensorResponse = new SensorResponse();
        sensorResponse.setSensorId(sensorId);
        sensorResponse.setTempData(tempData);
        sensorResponse.setHumidityData(humidityDate);
        return sensorResponse;
    }

    private List<SensorDataInfo> dummySensorRepoList() {
        List<SensorDataInfo> sensorDataInfoList = new ArrayList<>();
        sensorDataInfoList.add(sensorDataEntity("A001", "20", "300", "2023-03-10"));
        sensorDataInfoList.add(sensorDataEntity("A001", "10", "100", "2023-03-11"));
        sensorDataInfoList.add(sensorDataEntity("A001", "400", "2000", "2023-03-12"));
        return sensorDataInfoList;
    }

    @Test
    public void testProcessSave() {
        when(this.sensorDTOMapper.mapRequest(any(SensorRequest.class))).thenReturn(this.sensorDataInfo);
        when(this.sensorDTOMapper.mapResponse(any(SensorDataInfo.class))).thenReturn(sensorResponse("A001", 20, 300));
        when(this.iSensorRepo.saveSensorData(this.sensorDataInfo)).thenReturn(this.sensorDataInfo);
        SensorResponse actualResult = this.sensorProcessor.save(setUpSensorRequest());
        SensorResponse expected = sensorResponse("A001", 20, 300);
        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(expected, actualResult);
    }

    @Test
    public void testGetDataById() {
        when(this.iSensorRepo.findSensorDataById("A001")).thenReturn(dummySensorRepoList());
        when(this.sensorDTOMapper.mapResponse(any(SensorDataInfo.class))).thenReturn(sensorResponse("A001", 20, 300));
        List<SensorResponse> actualResult = this.sensorProcessor.getSensorDataList("A001");
        SensorResponse expected = sensorResponse("A001", 20, 300);
        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(expected, actualResult.get(0));
    }

    @Test
    public void getMetricsByIdInDateRange() {
        String dateFrom = "2023-03-10";
        String dateTo = "2023-03-12";
        when(this.iSensorRepo.findSensorIdBetween
                ("A001", LocalDate.parse(dateFrom), LocalDate.parse(dateTo)))
                .thenReturn(dummySensorRepoList());
        int[] tempData = {20, 40, 4};
        int[] humidityData = {300, 100, 2000};
        when(this.sensorDTOMapper.mapResponse(any(SensorDataInfo.class)))
                .thenReturn(
                        sensorResponse("A001", tempData[0], humidityData[0]),
                        sensorResponse("A001", tempData[1], humidityData[1]),
                        sensorResponse("A001", tempData[2], humidityData[2]));
        SensorResponse response = this.sensorProcessor.getMetricsByDateRange("A001", LocalDate.parse(dateFrom), LocalDate.parse(dateTo));

        Assertions.assertNotNull(response);

        //Temperature metrics assertions
        Assertions.assertEquals(Arrays.stream(tempData).max().getAsInt(), response.getTempMax());
        Assertions.assertEquals(Arrays.stream(tempData).min().getAsInt(), response.getTempMin());
        Assertions.assertEquals(Arrays.stream(tempData).average().getAsDouble(), response.getTempAvg());

        //Humidity metrics assertions
        Assertions.assertEquals(Arrays.stream(humidityData).max().getAsInt(), response.getHumidityMax());
        Assertions.assertEquals(Arrays.stream(humidityData).min().getAsInt(), response.getHumidityMin());
        Assertions.assertEquals(Arrays.stream(humidityData).average().getAsDouble(), response.getHumidityAvg());

        Assertions.assertEquals(String.valueOf(response.getDateFrom()), dateFrom);
        Assertions.assertEquals(String.valueOf(response.getDateTo()), dateTo);
    }

    @Test
    public void getMetricsByIdIfNoDateRange() {
        when(this.iSensorRepo.findSensorIdBetween(eq("A001"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(dummySensorRepoList());
        int[] tempData = {20, 40, 4};
        int[] humidityData = {300, 100, 2000};
        when(this.sensorDTOMapper.mapResponse(any(SensorDataInfo.class)))
                .thenReturn(
                        sensorResponse("A001", tempData[0], humidityData[0]),
                        sensorResponse("A001", tempData[1], humidityData[1]),
                        sensorResponse("A001", tempData[2], humidityData[2]));
        SensorResponse response = this.sensorProcessor.getMetricsByDateRange("A001", null, null);

        //Temperature metrics assertions
        Assertions.assertEquals(Arrays.stream(tempData).max().getAsInt(), response.getTempMax());
        Assertions.assertEquals(Arrays.stream(tempData).min().getAsInt(), response.getTempMin());
        Assertions.assertEquals(Arrays.stream(tempData).average().getAsDouble(), response.getTempAvg());

        //Humidity metrics assertions
        Assertions.assertEquals(Arrays.stream(humidityData).max().getAsInt(), response.getHumidityMax());
        Assertions.assertEquals(Arrays.stream(humidityData).min().getAsInt(), response.getHumidityMin());
        Assertions.assertEquals(Arrays.stream(humidityData).average().getAsDouble(), response.getHumidityAvg());

        //Asserting when no date is set,  from and to dates are today's date
        Assertions.assertEquals(response.getDateFrom(), LocalDate.now());
        Assertions.assertEquals(response.getDateTo(), LocalDate.now());
    }
}
