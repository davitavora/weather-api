package com.oddle.app.weather.repository;

import com.oddle.app.weather.domain.Weather;
import com.oddle.app.weather.model.Period;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
@Sql("classpath:db/weathers.sql")
class WeatherRepositoryTest {

    @Autowired
    WeatherRepository systemUnderTest;

    @Test
    public void should_find_weathers_by_city_name() {
        String city = "sample";
        List<Weather> weathers = systemUnderTest.findByNameIgnoreCase(city);
        assertEquals(3, weathers.size());
    }

    @Test
    public void should_find_weathers_by_period() {
        var period = new Period(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31));
        List<Weather> weathers = systemUnderTest.findByDtBetween(period.fromEpochSecond(), period.toEpochSecond());
        assertEquals(3, weathers.size());
    }

    @Test
    public void should_find_weathers_by_city_and_period() {
        var period = new Period(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31));
        List<Weather> weathers = systemUnderTest.findByNameIgnoreCaseAndDtBetween("sample1", period.fromEpochSecond(), period.toEpochSecond());
        assertEquals(1, weathers.size());
    }

}