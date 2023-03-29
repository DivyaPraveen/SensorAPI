package com.app.weather.controller;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionalHandlerTest {
    @InjectMocks
    GlobalExceptionalHandler globalExceptionalHandler;
    @Mock
    RuntimeException runtimeException;
    @Mock
    ResourceNotFoundException resourceNotFoundException;
    @Mock
    AmazonDynamoDBException amazonDynamoDBException;
    @Mock
    WebRequest webRequest;

    @Test
    public void testIllegalState() {
        when(runtimeException.getMessage()).thenReturn("IllegalStageException");
        ResponseEntity<Object> responseEntity= globalExceptionalHandler.illegalState(runtimeException, webRequest);
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CONFLICT,responseEntity.getStatusCode());
    }

    @Test
    public void testDbResourceNotFound(){
        when(resourceNotFoundException.getMessage()).thenReturn("Resource not found");
        ResponseEntity<Object> responseEntity= globalExceptionalHandler.dbResourceNotFound(resourceNotFoundException, webRequest);
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
    @Test
    public void testDbError(){
        String expected = "Unable to process the request because we cannot establish db connection";
        when(amazonDynamoDBException.getMessage()).thenReturn("we cannot establish db connection");
        ResponseEntity<Object> responseEntity= globalExceptionalHandler.dbError(amazonDynamoDBException, webRequest);
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        Assertions.assertEquals(expected,responseEntity.getBody());
    }

}
