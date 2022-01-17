package com.oddle.app.weather.controller;

import com.oddle.app.weather.exception.BadArgumentException;
import com.oddle.app.weather.model.Period;
import com.oddle.app.weather.model.dto.WeatherDTO;
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
    public List<WeatherDTO> findWeathersByCity(@RequestParam String city) {
        return weatherService.findWeathersBy(city);
    }

    @GetMapping(params = {"period"})
    public List<WeatherDTO> findWeathersByPeriod(@RequestParam Period period) {
        return weatherService.findWeathersBy(period);
    }

    @GetMapping(params = {"city", "period"})
    public List<WeatherDTO> findWeathersByCityAndPeriod(@RequestParam String city, @RequestParam Period period) {
        return weatherService.findWeathersBy(city, period);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public WeatherDTO save(@RequestBody WeatherDTO weather) {
        return weatherService.save(weather);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        weatherService.delete(id);
    }

    @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE)
    public WeatherDTO update(@PathVariable Long id, @RequestBody WeatherDTO weather) {
        if (!id.equals(weather.id())) throw new BadArgumentException("Path id is different from the provided weather id");
        return weatherService.update(weather);
    }

}