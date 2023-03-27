package com.app.weather.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WeatherConfigTest {

    @InjectMocks WeatherConfig weatherConfig;

    @Test
    public void testEndpoint(){
        AwsClientBuilder.EndpointConfiguration result = weatherConfig.localstackEndpoint();
        Assertions.assertNotNull(result);
    }
}
