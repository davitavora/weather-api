package com.oddle.app.weather.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

public record Period(
        LocalDate from,
        LocalDate to
) {

    public Long fromEpochSecond() {
        return from.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    }

    public Long toEpochSecond() {
        return to.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
    }

}
