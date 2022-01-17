package com.oddle.app.weather.service;

import com.oddle.app.weather.client.WeatherClient;
import com.oddle.app.weather.domain.Weather;
import com.oddle.app.weather.exception.WeatherNotFoundException;
import com.oddle.app.weather.mapper.WeatherMapper;
import com.oddle.app.weather.model.Period;
import com.oddle.app.weather.model.dto.WeatherDTO;
import com.oddle.app.weather.repository.WeatherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @InjectMocks
    private WeatherService systemUnderTest;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private WeatherRepository weatherRepository;

    @Nested
    @DisplayName("search weather")
    class SearchWeatherTest {

        @Test
        void should_find_weathers_by_city_when_provided_city_exists() {
            var city = "sample";
            var weatherEntity = new Weather(2L);
            var weatherDTO = new WeatherDTO(2L);
            when(weatherMapper.toDTO(anyList())).thenReturn(new ArrayList<>(Collections.singletonList(new WeatherDTO(1L))));
            when(weatherClient.findWeatherByCity(eq(city))).thenReturn(Optional.of(weatherDTO));
            when(weatherRepository.findByNameIgnoreCase(eq(city))).thenReturn(new ArrayList<>(Collections.singletonList(weatherEntity)));

            var weathers = systemUnderTest.findWeathersBy(city);
            assertEquals(2, weathers.size());
        }

        @Test
        void should_find_weathers_by_city_and_return_only_weathers_from_remote_when_doesnt_exists_weathers_locally() {
            var city = "sample";
            var weatherDTO = new WeatherDTO(2L);
            when(weatherClient.findWeatherByCity(eq(city))).thenReturn(Optional.of(weatherDTO));
            var weathers = systemUnderTest.findWeathersBy(city);
            assertEquals(1, weathers.size());
        }

        @Test
        void should_find_weathers_by_city_and_return_only_weathers_from_local_when_doesnt_exists_weathers_remotely() {
            var city = "sample";
            var weatherEntity1 = new Weather(1L);
            var weatherEntity2 = new Weather(2L);
            when(weatherMapper.toDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new WeatherDTO(1L), new WeatherDTO(2L))));
            when(weatherRepository.findByNameIgnoreCase(eq(city))).thenReturn(new ArrayList<>(Arrays.asList(weatherEntity1, weatherEntity2)));

            var weathers = systemUnderTest.findWeathersBy(city);
            assertEquals(2, weathers.size());
        }

        @Test
        void should_find_weathers_by_period_when_they_exists() {
            var period = new Period(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31));
            when(weatherMapper.toDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new WeatherDTO(1L), new WeatherDTO(2L))));
            when(weatherRepository.findByDtBetween(eq(period.fromEpochSecond()), eq(period.toEpochSecond()))).thenReturn(new ArrayList<>(Arrays.asList(new Weather(1L), new Weather(2L))));

            var weathers = systemUnderTest.findWeathersBy(period);

            assertEquals(2, weathers.size());
            verify(weatherRepository, times(1)).findByDtBetween(period.fromEpochSecond(), period.toEpochSecond());
        }

        @Test
        void should_find_weathers_by_city_and_period_when_they_exists() {
            var city = "sample";
            LocalDate from = LocalDate.of(2022, 1, 1);
            LocalDate to = LocalDate.of(2022, 1, 31);
            var period = new Period(from, to);
            when(weatherMapper.toDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new WeatherDTO(1L), new WeatherDTO(2L))));
            when(weatherClient.findWeatherByCity(city)).thenReturn(Optional.of(new WeatherDTO(3L, period.fromEpochSecond())));
            when(weatherRepository.findByNameIgnoreCaseAndDtBetween(city, period.fromEpochSecond(), period.toEpochSecond())).thenReturn(new ArrayList<>(Arrays.asList(new Weather(1L), new Weather(2L))));

            List<WeatherDTO> weathers = systemUnderTest.findWeathersBy(city, period);
            assertEquals(3, weathers.size());
        }

        @Test
        void should_find_weathers_by_city_and_period_filtering_remote_weathers_by_date_when_they_exists_but_out_of_range() {
            var city = "sample";
            LocalDate from = LocalDate.of(2022, 1, 1);
            LocalDate to = LocalDate.of(2022, 1, 31);
            var period = new Period(from, to);
            when(weatherMapper.toDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new WeatherDTO(1L), new WeatherDTO(2L))));
            when(weatherClient.findWeatherByCity(city)).thenReturn(Optional.of(new WeatherDTO(3L, from.minusDays(1).toEpochDay())));
            when(weatherRepository.findByNameIgnoreCaseAndDtBetween(city, period.fromEpochSecond(), period.toEpochSecond())).thenReturn(new ArrayList<>(Arrays.asList(new Weather(1L), new Weather(2L))));

            List<WeatherDTO> weathers = systemUnderTest.findWeathersBy(city, period);
            assertEquals(2, weathers.size());
        }

    }

    @Nested
    class SaveWeatherTest {

        @Test
        void should_save_weather() {
            WeatherDTO weatherToSave = systemUnderTest.save(new WeatherDTO(1L));
            when(weatherMapper.toEntity(eq(weatherToSave))).thenReturn(new Weather(1L));
            when(weatherRepository.save(any(Weather.class))).then(returnsFirstArg());
            when(weatherMapper.toDTO(any(Weather.class))).thenReturn(new WeatherDTO(1L));

            WeatherDTO savedWeather = systemUnderTest.save(weatherToSave);

            assertNotNull(savedWeather);
            assertNotNull(savedWeather.id());
        }

    }

    @Nested
    class DeleteWeatherTest {

        @Test
        void should_delete_weather() {
            when(weatherRepository.findById(eq(1L))).thenReturn(Optional.of(new Weather(1L)));

            systemUnderTest.delete(1L);

            verify(weatherRepository, times(1)).delete(any(Weather.class));
        }

        @Test
        void should_throw_exception_when_weather_with_provided_id_doesnt_exists() {
            when(weatherRepository.findById(eq(1L))).thenReturn(Optional.empty());

            var exception = assertThrows(WeatherNotFoundException.class, () -> systemUnderTest.delete(1L));
            assertEquals(exception.getMessage(), "There is no weather with id 1");
        }

    }

    @Nested
    class UpdateWeatherTest {

        @Test
        void should_update_a_weather() {
            WeatherDTO weatherToUpdate = new WeatherDTO(1L, 1L);
            when(weatherRepository.existsById(eq(weatherToUpdate.id()))).thenReturn(true);
            when(weatherMapper.toEntity(eq(weatherToUpdate))).thenReturn(new Weather(1L));
            when(weatherRepository.save(any(Weather.class))).then(returnsFirstArg());
            when(weatherMapper.toDTO(any(Weather.class))).thenReturn(new WeatherDTO(1L));

            WeatherDTO updatedWeather = systemUnderTest.update(weatherToUpdate);

            assertEquals(weatherToUpdate.id(), updatedWeather.id());
        }

        @Test
        void should_throw_exception_when_weather_with_provided_weather_doesnt_exists() {
            WeatherDTO weatherToUpdate = new WeatherDTO(1L, 1L);
            when(weatherRepository.existsById(eq(weatherToUpdate.id()))).thenReturn(false);

            assertThrows(WeatherNotFoundException.class, () -> systemUnderTest.update(weatherToUpdate));
        }

    }

}