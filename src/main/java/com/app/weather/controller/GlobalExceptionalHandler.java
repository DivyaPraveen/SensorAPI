package com.app.weather.controller;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
//Todo: Extend an error class instead of ResponseEntity<Object> to respond in some code and message
public class GlobalExceptionalHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class})
    protected ResponseEntity<Object> illegalState(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage(); // Todo: Refine error message as per the requirement
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler(value
            = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> dbResourceNotFound(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = {AmazonDynamoDBException.class})
    protected ResponseEntity<Object> dbError(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Unable to process the request because " + ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}