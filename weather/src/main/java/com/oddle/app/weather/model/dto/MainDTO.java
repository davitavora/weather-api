package com.oddle.app.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainDTO(
        Double temp,
        @JsonProperty("feels_like") Double feelsLike,
        @JsonProperty("temp_min") Double tempMin,
        @JsonProperty("temp_max") Double tempMax,
        Integer pressure,
        Integer humidity,
        @JsonProperty("sea_level") Integer seaLevel,
        @JsonProperty("grnd_level") Integer grndLevel
) {

}
