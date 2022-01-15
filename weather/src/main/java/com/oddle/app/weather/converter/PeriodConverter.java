package com.oddle.app.weather.converter;

import com.oddle.app.weather.exception.BadArgumentException;
import com.oddle.app.weather.model.Period;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class PeriodConverter implements Converter<String, Period> {

    private final static String PERIOD_PATTERN = "^\\d{4}-\\d{2}-\\d{2},\\d{4}-\\d{2}-\\d{2}$";

    @Override
    public Period convert(String source) {
        validate(source.isEmpty(), "period should not be empty");
        validate(!source.matches(PERIOD_PATTERN), "period should match the following format: 'yyyy-MM-dd,yyyy-MM-dd'");
        String[] dates = source.split(",");
        LocalDate from = parseDate(dates[0], "provided 'from date' is invalid");
        LocalDate to = parseDate(dates[1], "provided 'to date' is invalid");
        validate(from.isAfter(to), "'from date' should be before 'to date'");
        return new Period(from, to);
    }

    private void validate(boolean condition, String errorMessage) {
        if (condition) throw new BadArgumentException(errorMessage);
    }

    private LocalDate parseDate(String date, String errorMessage) {
        LocalDate from;
        try {
            from = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new BadArgumentException(errorMessage);
        }
        return from;
    }

}
