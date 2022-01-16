package com.oddle.app.weather.model;

public record Condition(
        Integer id,
        String main,
        String description,
        String icon
) {

}
