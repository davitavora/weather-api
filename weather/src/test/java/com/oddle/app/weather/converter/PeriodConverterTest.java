package com.oddle.app.weather.converter;

import com.oddle.app.weather.exception.BadArgumentException;
import com.oddle.app.weather.model.Period;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class PeriodConverterTest {

    PeriodConverter systemUnderTest = new PeriodConverter();

    @Test
    public void should_throw_exception_when_period_is_empty() {
        BadArgumentException exception = Assertions.assertThrows(BadArgumentException.class, () -> systemUnderTest.convert(""));
        Assertions.assertEquals(exception.getMessage(), "period should not be empty");
    }

    @Test
    public void should_throw_exception_when_period_has_invalid_format() {
        BadArgumentException exception = Assertions.assertThrows(BadArgumentException.class, () -> systemUnderTest.convert("2022-01-14"));
        Assertions.assertEquals(exception.getMessage(), "period should match the following format: 'yyyy-MM-dd,yyyy-MM-dd'");
    }

    @Test
    public void should_throw_exception_when_from_date_is_invalid() {
        BadArgumentException exception = Assertions.assertThrows(BadArgumentException.class, () -> systemUnderTest.convert("2022-02-30,2022-03-01"));
        Assertions.assertEquals(exception.getMessage(), "provided 'from date' is invalid");
    }

    @Test
    public void should_throw_exception_when_to_date_is_invalid() {
        BadArgumentException exception = Assertions.assertThrows(BadArgumentException.class, () -> systemUnderTest.convert("2022-01-31,2022-02-30"));
        Assertions.assertEquals(exception.getMessage(), "provided 'to date' is invalid");
    }

    @Test
    public void should_throw_exception_when_from_date_is_after_to_date() {
        BadArgumentException exception = Assertions.assertThrows(BadArgumentException.class, () -> systemUnderTest.convert("2022-03-31,2022-02-20"));
        Assertions.assertEquals(exception.getMessage(), "'from date' should be before 'to date'");
    }

    @Test
    public void should_convert_period_when_provided_data_is_valid() {
        Period period = systemUnderTest.convert("2022-01-14,2022-01-30");
        Assertions.assertNotNull(period);
        Assertions.assertEquals(period.getFrom(), LocalDate.of(2022, 1, 14));
        Assertions.assertEquals(period.getTo(), LocalDate.of(2022, 1, 30));
    }

}