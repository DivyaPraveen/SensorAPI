package com.app.weather.mapper;

import com.app.weather.models.SensorDataInfo;
import com.app.weather.models.SensorRequest;
import com.app.weather.models.SensorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {SensorDTOMapperImpl.class})
public class SensorDTOMapperTest {
    @Autowired
    private SensorDTOMapper sensorDTOMapper;

    private SensorRequest setUpSensorRequest() {
        SensorRequest sensorRequest = new SensorRequest();
        sensorRequest.setSensorId("100");
        sensorRequest.setTempData("32");
        sensorRequest.setHumidityData("80");
        return sensorRequest;
    }

    private SensorDataInfo sensorDataEntity() {
        SensorDataInfo sensorDataInfo = new SensorDataInfo();
        sensorDataInfo.setSensorId("100");
        sensorDataInfo.setTempData("32");
        sensorDataInfo.setHumidityData("80");
        sensorDataInfo.setCreateTs(LocalDate.now());
        return sensorDataInfo;
    }

    private SensorResponse sensorResponse(){
        SensorResponse sensorResponse= new SensorResponse();
        sensorResponse.setSensorId("100");
        sensorResponse.setTempData(32);
        sensorResponse.setHumidityData(80);
        return sensorResponse;
    }

    /**
     * Given SensorRequest
     * When mapping
     * Then exact entity to be persisted is created
     */
    @Test
    public void testSensorRequestMapping() {
        SensorDataInfo actualSensorData = sensorDTOMapper.mapRequest(setUpSensorRequest());
        SensorDataInfo expectedSensorData = sensorDataEntity();
        Assertions.assertNotNull(actualSensorData);
        Assertions.assertEquals(expectedSensorData, actualSensorData);
    }
    /**
     * Given Sensor Data entity
     * When mapping
     * Then response to controller is created
     */
    @Test
    public void testSensorResponseMapping() {
        SensorResponse actualSensorData = sensorDTOMapper.mapResponse(sensorDataEntity());
        SensorResponse expectedSensorData = sensorResponse();
        Assertions.assertNotNull(actualSensorData);
        Assertions.assertEquals(expectedSensorData, actualSensorData);
    }
}
