package com.oddle.app.weather.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Period {

    private final LocalDate from;

    private final LocalDate to;

}
