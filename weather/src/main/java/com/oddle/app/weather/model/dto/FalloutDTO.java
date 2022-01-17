package com.oddle.app.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FalloutDTO(
        @JsonProperty("1h") Double oneHour,
        @JsonProperty("3h") Double threeHour
) {

}
