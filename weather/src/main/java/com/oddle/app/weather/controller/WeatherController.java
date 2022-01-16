package com.oddle.app.weather.controller;

import com.oddle.app.weather.exception.BadArgumentException;
import com.oddle.app.weather.model.Period;
import com.oddle.app.weather.model.Weather;
import com.oddle.app.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "weathers", produces = APPLICATION_JSON_VALUE)
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping(params = {"city"})
    public List<Weather> findWeathersByCity(@RequestParam String city) {
        return weatherService.findWeathersBy(city);
    }

    @GetMapping(params = {"period"})
    public List<Weather> findWeathersByPeriod(@RequestParam Period period) {
        return weatherService.findWeathersBy(period);
    }

    @GetMapping(params = {"city", "period"})
    public List<Weather> findWeathersByCityAndPeriod(@RequestParam String city, @RequestParam Period period) {
        return weatherService.findWeathersBy(city, period);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Weather save(@RequestBody Weather weather) {
        return weatherService.save(weather);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        weatherService.delete(id);
    }

    @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE)
    public Weather update(@PathVariable Long id, @RequestBody Weather weather) {
        if (!id.equals(weather.id())) throw new BadArgumentException("Path id is different from the provided weather id");
        return weatherService.update(weather);
    }

}