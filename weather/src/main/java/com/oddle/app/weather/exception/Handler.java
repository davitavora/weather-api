package com.oddle.app.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadArgumentException.class)
    public ErrorResponse handleBadArgumentException(BadArgumentException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WeatherNotFoundException.class)
    public ErrorResponse handleWeatherNotFoundException(WeatherNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
