package com.oddle.app.weather.client;

import com.oddle.app.weather.model.dto.WeatherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "weatherClient", url = "${weather.client.url}", path = "/data/2.5/weather")
public interface WeatherClient {

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Optional<WeatherDTO> findWeatherByCity(@RequestParam("q") String city);

}
