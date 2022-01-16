package com.oddle.app.weather.model;

import java.time.LocalDate;

public record Period(
        LocalDate from,
        LocalDate to
) {

}
