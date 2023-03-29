package com.app.weather.mapper;

import com.app.weather.models.SensorDataInfo;
import com.app.weather.models.SensorRequest;
import com.app.weather.models.SensorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Mapper(imports = {LocalDate.class})
@MapperConfig(
        componentModel = "spring")
public interface SensorDTOMapper {
    /**
     * This mapper converts SensorRequest to  SensorDataInfo
     * createts is set to the current date
     * @param request
     * @return
     */
    @Mapping(target = "createTs", expression = "java(LocalDate.now())")
    SensorDataInfo mapRequest(SensorRequest request);

    /**
     * This mapper converts SensorDataInfo to SensorResponse
     * @param dataInfo
     * @return
     */
    SensorResponse mapResponse(SensorDataInfo dataInfo);

}
