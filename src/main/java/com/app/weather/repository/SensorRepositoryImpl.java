package com.app.weather.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.app.weather.models.SensorDataInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Data
@Slf4j
@Configuration
public class SensorRepositoryImpl implements ISensorRepo {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public SensorRepositoryImpl(@Qualifier("getDynamoDBMapper") final DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public SensorDataInfo saveSensorData(SensorDataInfo sensorDataInfo) {
        this.dynamoDBMapper.save(sensorDataInfo);
        log.info("Saving sensor data infos to table with sensorId: " + sensorDataInfo.getSensorId());
        return sensorDataInfo;
    }

    @Override
    public List<SensorDataInfo> findSensorDataById(String sensorId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("sensorId", new Condition().withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(sensorId)));
        log.info("Querying with sensorId: " + sensorId);
        //Todo: Change the expression to do a query instead of scan
        return dynamoDBMapper.scan(SensorDataInfo.class, scanExpression);
    }

    @Override
    public List<SensorDataInfo> findSensorIdBetween(String sensorId, LocalDate dateFrom, LocalDate dateTo) {
        List<SensorDataInfo> sensorDataInfoList = new ArrayList<>();
        log.info(String.format("Querying with sensorId: %s and date range between %s and %s  ", sensorId, dateFrom, dateTo));
        final Map<String, AttributeValue> expressionAttributeValue = new HashMap<>();
        expressionAttributeValue.put(":val1", new AttributeValue().withS(dateFrom.toString()));
        expressionAttributeValue.put(":val2", new AttributeValue().withS(dateTo.toString()));
        expressionAttributeValue.put(":sensorId", new AttributeValue().withS(sensorId));


        final DynamoDBQueryExpression<SensorDataInfo> dynamoDBQueryExpression =
                new DynamoDBQueryExpression<SensorDataInfo>()
                        .withIndexName("sensoridCreateTs")
                        .withKeyConditionExpression(
                                "sensorId = :sensorId and createTs between :val1 and :val2")
                        .withExpressionAttributeValues(expressionAttributeValue)
                        .withConsistentRead(false);

        sensorDataInfoList.addAll(
                dynamoDBMapper.query(SensorDataInfo.class, dynamoDBQueryExpression));
        return sensorDataInfoList;
    }
}


