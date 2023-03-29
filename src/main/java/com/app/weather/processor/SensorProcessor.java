package com.app.weather.processor;

import com.app.weather.mapper.SensorDTOMapper;
import com.app.weather.models.SensorDataInfo;
import com.app.weather.models.SensorRequest;
import com.app.weather.models.SensorResponse;
import com.app.weather.processor.util.SensorProcessorUtil;
import com.app.weather.repository.ISensorRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class SensorProcessor extends SensorProcessorUtil {
    private final SensorDTOMapper sensorDTOMapper;
    private final ISensorRepo sensorRepository;

    @Autowired
    public SensorProcessor(@Qualifier("sensorDTOMapper") SensorDTOMapper sensorDTOMapper, ISensorRepo sensorRepository) {
        this.sensorDTOMapper = sensorDTOMapper;
        this.sensorRepository = sensorRepository;
    }


    public SensorResponse save(SensorRequest sensorRequest) {
        SensorDataInfo sensorDataInfo = sensorDTOMapper.mapRequest(sensorRequest);
        SensorDataInfo responseSensorData = sensorRepository.saveSensorData(sensorDataInfo);
        return sensorDTOMapper.mapResponse(responseSensorData);
    }

    public List<SensorResponse> getSensorDataList(String sensorId) {
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        List<SensorDataInfo> sensorDataInfoList = sensorRepository.findSensorDataById(sensorId);
        for (SensorDataInfo sensorDataInfo :
                sensorDataInfoList) {
            sensorResponseList.add(sensorDTOMapper.mapResponse(sensorDataInfo));
        }
        return sensorResponseList;
    }

    public SensorResponse getMetricsByDateRange(String sensorId, LocalDate dateFrom, LocalDate dateTo) {
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        List<SensorDataInfo> sensorDataList = null;
        LocalDate todayDate = LocalDate.now();
        if (dateFrom == null || dateTo == null) {
            sensorDataList = sensorRepository.findSensorIdBetween(sensorId, todayDate, todayDate);
        } else {
            sensorDataList = sensorRepository.findSensorIdBetween(sensorId, dateFrom, dateTo);
        }

        for (SensorDataInfo sensorDataInfo :
                sensorDataList) {
            sensorResponseList.add(sensorDTOMapper.mapResponse(sensorDataInfo));
        }
        if (!sensorResponseList.isEmpty()) {
            SensorResponse response = findMetrics(sensorResponseList);
            response.setDateFrom(LocalDate.parse(dateFrom != null ? dateFrom.toString() : todayDate.toString()));
            response.setDateTo(LocalDate.parse(dateTo != null ? dateTo.toString() : todayDate.toString()));
            return response;
        }
        return null;
    }
}