package com.oddle.app.weather.model.dto;

public record SysDTO(
        Integer type,
        Integer id,
        Double message,
        String country,
        Long sunrise,
        Long sunset
) {

}
