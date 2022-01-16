package com.oddle.app.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Weather(
        Coord coord,
        @JsonProperty("weather") List<Condition> conditions,
        String base,
        Main main,
        Integer visibility,
        Wind wind,
        Clouds clouds,
        Fallout snow,
        Fallout rain,
        Long dt,
        Sys sys,
        Integer timezone,
        Long id,
        String name,
        Integer cod
) {

    public Weather(Long id) {
        this(null, null, null, null, null, null, null, null, null, null, null, null, id, null, null);
    }

}
