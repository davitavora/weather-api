package com.oddle.app.weather.model.dto;

public record ConditionDTO(
        Integer id,
        String main,
        String description,
        String icon
) {

}
