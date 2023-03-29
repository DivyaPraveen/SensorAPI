package com.app.weather.mapper;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;

/**
 * This class is for Dynamo db to convert the LocalDate to string
 */
public class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(LocalDate date) {
        return date.toString();
    }

    @Override
    public LocalDate unconvert(final String stringValue) {
        return LocalDate.parse(stringValue);
    }
}