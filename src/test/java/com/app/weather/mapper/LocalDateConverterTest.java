package com.app.weather.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
public class LocalDateConverterTest {
    private static final String dateString = "2020-01-08";
    private static final LocalDate localDate = LocalDate.of(2020, 1, 8);

    @Test
    public void testConvert() {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        String actual = localDateConverter.convert(localDate);
        Assertions.assertEquals(dateString, actual);
    }

    @Test
    public void testUnConvert() {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        LocalDate actual = localDateConverter.unconvert(dateString);
        Assertions.assertEquals(localDate, actual);
    }
}
