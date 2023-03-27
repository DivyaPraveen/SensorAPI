package com.app.weather.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.app.weather.models.SensorDataInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SensorRepositoryImplTest {

    @InjectMocks
    private SensorRepositoryImpl sensorRepository;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    PaginatedScanList<SensorDataInfo> paginatedScanList;

    @Mock
    PaginatedQueryList<SensorDataInfo> paginatedQueryList;

    private SensorDataInfo sensorDataEntity(String sensorId, String tempDate, String humidityDate, String createts) {
        SensorDataInfo sensorDataInfo = new SensorDataInfo();
        sensorDataInfo.setSensorId(sensorId);
        sensorDataInfo.setTempData(tempDate);
        sensorDataInfo.setHumidityData(humidityDate);
        sensorDataInfo.setCreateTs(LocalDate.parse(createts));
        return sensorDataInfo;
    }

    @Test
    public void testSave() {
        doNothing().when(this.sensorRepository.getDynamoDBMapper()).save(any(SensorDataInfo.class));
        SensorDataInfo expected = sensorDataEntity("100", "20", "300", String.valueOf(LocalDate.now()));
        SensorDataInfo result = this.sensorRepository.saveSensorData(expected);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testFindById() {
        when(dynamoDBMapper.scan(eq(SensorDataInfo.class), any(DynamoDBScanExpression.class)))
                .thenReturn(mock(PaginatedScanList.class, withSettings().defaultAnswer(new ForwardsInvocations(dummySensorRepoList()))));
        List<SensorDataInfo> result = this.sensorRepository.findSensorDataById("100");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(dummySensorRepoList().size(), result.size());
    }

    @Test
    void testMetrics() {
        List<SensorDataInfo> sensorDataInfoList = dummySensorRepoList();
        when(dynamoDBMapper.query(eq(SensorDataInfo.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(sensorDataInfoList))));
        List<SensorDataInfo> result = this.sensorRepository.findSensorIdBetween("A001", LocalDate.parse("2023-03-10"), LocalDate.parse("2023-03-12"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(sensorDataInfoList.size(), result.size());
    }

    private List<SensorDataInfo> dummySensorRepoList() {
        List<SensorDataInfo> sensorDataInfoList = new ArrayList<>();
        sensorDataInfoList.add(sensorDataEntity("A001", "20", "300", String.valueOf("2023-03-10")));
        sensorDataInfoList.add(sensorDataEntity("A001", "10", "100", String.valueOf("2023-03-11")));
        sensorDataInfoList.add(sensorDataEntity("A001", "400", "2000", String.valueOf("2023-03-12")));
        return sensorDataInfoList;
    }

}
