package com.oddle.app.weather.service;

import com.oddle.app.weather.model.Period;
import com.oddle.app.weather.model.Weather;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {

    public List<Weather> findWeathersBy(String city) {
        throw new UnsupportedOperationException();
    }

    public List<Weather> findWeathersBy(Period period) {
        throw new UnsupportedOperationException();
    }

    public List<Weather> findWeathersBy(String city, Period period) {
        throw new UnsupportedOperationException();
    }

    public Weather save(Weather weather) {
        throw new UnsupportedOperationException();
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }

    public Weather update(Weather weather) {
        throw new UnsupportedOperationException();
    }

}
