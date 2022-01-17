package com.oddle.app.weather.repository;

import com.oddle.app.weather.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    List<Weather> findByNameIgnoreCase(String name);

    List<Weather> findByDtBetween(Long from, Long to);

    List<Weather> findByNameIgnoreCaseAndDtBetween(String city, Long from, Long to);

}
