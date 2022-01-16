package com.oddle.app.weather.exception;

public class BadArgumentException extends RuntimeException {

    public BadArgumentException(String message) {
        super(message);
    }

}