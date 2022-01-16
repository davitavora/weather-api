package com.oddle.app.weather.model;

public record Sys(
        Integer type,
        Integer id,
        Double message,
        String country,
        Long sunrise,
        Long sunset
) {

}
