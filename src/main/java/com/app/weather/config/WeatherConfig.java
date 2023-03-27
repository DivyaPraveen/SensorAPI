package com.app.weather.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.app.weather.mapper.SensorDTOMapper;
import com.app.weather.mapper.SensorDTOMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
//Todo: Extend this methods to adapt to AWS
public class WeatherConfig{

    public static final String HTTP_LOCALHOST_4566 = "http://localhost:4566";
    private static final String LOCALSTACK_LOCAL_REGION = "us-east-1"; // default region
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;

    @Bean
    public EndpointConfiguration localstackEndpoint() {
        return new EndpointConfiguration(
                HTTP_LOCALHOST_4566, LOCALSTACK_LOCAL_REGION);
    }

    @Bean
    public DynamoDBMapper getDynamoDBMapper(
            @Qualifier("localstackEndpoint") EndpointConfiguration localstackEndpoint,
            @Qualifier("dynamoDBMapperConfig") DynamoDBMapperConfig dynamoDBMapperConfig) {

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(localstackEndpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();

        return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);

    }


    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {

        return DynamoDBMapperConfig.builder()
                .withTableNameResolver((aClass, dynamoDBMapperConfig) ->
                        "SensorDataInfo")
                .build();
    }

    @Bean
    public SensorDTOMapper sensorDTOMapper() {
        return new SensorDTOMapperImpl();
    }

}
