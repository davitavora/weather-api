package com.oddle.app.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Fallout(
        @JsonProperty("1h") Double oneHour,
        @JsonProperty("3h") Double threeHour
) {

}
