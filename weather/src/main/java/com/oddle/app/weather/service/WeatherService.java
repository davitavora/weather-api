package com.oddle.app.weather.service;

import com.oddle.app.weather.client.WeatherClient;
import com.oddle.app.weather.domain.Weather;
import com.oddle.app.weather.exception.WeatherNotFoundException;
import com.oddle.app.weather.mapper.WeatherMapper;
import com.oddle.app.weather.model.Period;
import com.oddle.app.weather.model.dto.WeatherDTO;
import com.oddle.app.weather.repository.WeatherRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherMapper weatherMapper;
    private final WeatherClient weatherClient;
    private final WeatherRepository weatherRepository;

    public List<WeatherDTO> findWeathersBy(String city) {
        var weathers = weatherRepository.findByNameIgnoreCase(city);
        var weathersToReturn = weatherMapper.toDTO(weathers);
        findRemoteWeather(city).ifPresent(weathersToReturn::add);
        return weathersToReturn;
    }

    public List<WeatherDTO> findWeathersBy(Period period) {
        var weathers = weatherRepository.findByDtBetween(period.fromEpochSecond(), period.toEpochSecond());
        return weatherMapper.toDTO(weathers);
    }

    public List<WeatherDTO> findWeathersBy(String city, Period period) {
        var weathers = weatherRepository.findByNameIgnoreCaseAndDtBetween(city, period.fromEpochSecond(), period.toEpochSecond());
        var weathersToReturn = weatherMapper.toDTO(weathers);
        findRemoteWeather(city).filter(weatherDTO -> weatherDTO.isBetweenPeriod(period)).ifPresent(weathersToReturn::add);
        return weathersToReturn;
    }

    public WeatherDTO save(WeatherDTO weatherDTO) {
        var weather = weatherMapper.toEntity(weatherDTO);
        weather = weatherRepository.save(weather);
        return weatherMapper.toDTO(weather);
    }

    public void delete(Long id) {
        weatherRepository.findById(id).ifPresentOrElse(
                weatherRepository::delete,
                () -> {
                    throw new WeatherNotFoundException("There is no weather with id " + id);
                }
        );
    }

    public WeatherDTO update(WeatherDTO weather) {
        if (weatherRepository.existsById(weather.id())) {
            Weather savedWeather = weatherRepository.save(weatherMapper.toEntity(weather));
            return weatherMapper.toDTO(savedWeather);
        }
        throw new WeatherNotFoundException("There is no weather with id " + weather.id());
    }

    private Optional<WeatherDTO> findRemoteWeather(String city) {
        try {
            return weatherClient.findWeatherByCity(city);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

}
