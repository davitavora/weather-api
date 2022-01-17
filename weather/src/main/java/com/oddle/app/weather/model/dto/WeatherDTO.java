package com.oddle.app.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oddle.app.weather.model.Period;

import java.util.List;

public record WeatherDTO(
        CoordDTO coord,
        @JsonProperty("weather") List<ConditionDTO> conditions,
        String base,
        MainDTO main,
        Integer visibility,
        WindDTO wind,
        CloudsDTO clouds,
        FalloutDTO snow,
        FalloutDTO rain,
        Long dt,
        SysDTO sys,
        Integer timezone,
        Long id,
        String name,
        Integer cod
) {

    public WeatherDTO(Long id) {
        this(null, null, null, null, null, null, null, null, null, null, null, null, id, null, null);
    }

    public WeatherDTO(Long id, Long dt) {
        this(null, null, null, null, null, null, null, null, null, dt, null, null, id, null, null);
    }

    public boolean isBetweenPeriod(Period period) {
        return dt >= period.fromEpochSecond() && dt() <= period.toEpochSecond();
    }

}
