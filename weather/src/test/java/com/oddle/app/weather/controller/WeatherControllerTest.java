package com.oddle.app.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oddle.app.weather.exception.WeatherNotFoundException;
import com.oddle.app.weather.model.Period;
import com.oddle.app.weather.model.Weather;
import com.oddle.app.weather.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WeatherService service;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("search weather")
    class SearchWeatherTest {

        @Test
        public void should_return_a_list_of_weathers_when_only_city_is_provided() throws Exception {
            String city = "sample";
            when(service.findWeathersBy(eq(city))).thenReturn(singletonList(new Weather(1L)));

            mockMvc
                    .perform(
                            get("/weathers")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .queryParam("city", city)
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$", hasSize(1))
                    );
        }

        @Test
        public void should_return_a_list_of_weathers_filtered_by_period_when_only_period_is_provided() throws Exception {
            Period period = new Period(LocalDate.of(2022, 1, 14), LocalDate.of(2022, 1, 30));
            when(service.findWeathersBy(eq(period))).thenReturn(asList(new Weather(1L), new Weather(1L)));

            mockMvc.perform(
                    get("/weathers")
                            .accept(MediaType.APPLICATION_JSON)
                            .queryParam("period", "2022-01-14,2022-01-30")
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$", hasSize(2))
            );
        }

        @Test
        public void should_return_a_list_of_weathers_filtered_by_city_and_period_when_city_and_period_are_provided() throws Exception {
            String city = "sample";
            Period period = new Period(LocalDate.of(2022, 1, 14), LocalDate.of(2022, 1, 31));
            when(service.findWeathersBy(eq(city), eq(period))).thenReturn(asList(new Weather(1L), new Weather(1L)));

            mockMvc.perform(
                    get("/weathers")
                            .accept(MediaType.APPLICATION_JSON)
                            .queryParam("city", "sample")
                            .queryParam("period", "2022-01-14,2022-01-31")
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$", hasSize(2))
            );
        }

        @Test
        public void should_return_a_error_when_period_is_empty() throws Exception {
            mockMvc.perform(
                            get("/weathers")
                                    .queryParam("period", "")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.message", is("period should not be empty"))
                    );
        }

        @Test
        public void should_return_a_error_when_period_has_invalid_format() throws Exception {
            mockMvc.perform(
                            get("/weathers")
                                    .queryParam("period", "2022-01-14")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.message", is("period should match the following format: 'yyyy-MM-dd,yyyy-MM-dd'"))
                    );
        }

        @Test
        public void should_return_a_error_when_from_date_is_invalid() throws Exception {
            mockMvc.perform(
                            get("/weathers")
                                    .queryParam("period", "2022-02-30,2022-03-01")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.message", is("provided 'from date' is invalid"))
                    );
        }

        @Test
        public void should_return_a_error_when_to_date_is_invalid() throws Exception {
            mockMvc.perform(
                            get("/weathers")
                                    .queryParam("period", "2022-01-31,2022-02-30")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.message", is("provided 'to date' is invalid"))
                    );
        }

        @Test
        public void should_return_a_error_when_from_date_is_after_to_date() throws Exception {
            mockMvc.perform(
                            get("/weathers")
                                    .queryParam("period", "2022-03-31,2022-02-20")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.message", is("'from date' should be before 'to date'"))
                    );
        }

    }

    @Nested
    class SaveWeatherTest {

        @Test
        public void should_save_a_weather_when_a_valid_weather_is_provided() throws Exception {
            when(service.save(any(Weather.class))).then(returnsFirstArg());

            mockMvc.perform(
                    post("/weathers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    	"coord": {
                                    		"lon": -122.08,
                                    		"lat": 37.39
                                    	},
                                    	"weather": [{
                                    		"id": 800,
                                    		"main": "Clear",
                                    		"description": "clear sky",
                                    		"icon": "01d"
                                    	}],
                                    	"base": "stations",
                                    	"main": {
                                    		"temp": 282.55,
                                    		"feels_like": 281.86,
                                    		"temp_min": 280.37,
                                    		"temp_max": 284.26,
                                    		"pressure": 1023,
                                    		"humidity": 100
                                    	},
                                    	"visibility": 16093,
                                    	"wind": {
                                    		"speed": 1.5,
                                    		"deg": 350
                                    	},
                                    	"clouds": {
                                    		"all": 1
                                    	},
                                    	"dt": 1560350645,
                                    	"sys": {
                                    		"type": 1,
                                    		"id": 5122,
                                    		"message": 0.0139,
                                    		"country": "US",
                                    		"sunrise": 1560343627,
                                    		"sunset": 1560396563
                                    	},
                                    	"timezone": -25200,
                                    	"id": 420006353,
                                    	"name": "Mountain View",
                                    	"cod": 200
                                    }
                                    """)
            ).andExpectAll(
                    status().isCreated(),
                    jsonPath("$.coord.lon", is(-122.08)),
                    jsonPath("$.coord.lat", is(37.39)),
                    jsonPath("$.weather.[0].id", is(800)),
                    jsonPath("$.weather.[0].main", is("Clear")),
                    jsonPath("$.weather.[0].description", is("clear sky")),
                    jsonPath("$.weather.[0].icon", is("01d")),
                    jsonPath("$.base", is("stations")),
                    jsonPath("$.main.temp", is(282.55)),
                    jsonPath("$.main.feels_like", is(281.86)),
                    jsonPath("$.main.temp_min", is(280.37)),
                    jsonPath("$.main.temp_max", is(284.26)),
                    jsonPath("$.main.pressure", is(1023)),
                    jsonPath("$.main.humidity", is(100)),
                    jsonPath("$.visibility", is(16093)),
                    jsonPath("$.wind.speed", is(1.5)),
                    jsonPath("$.wind.deg", is(350.0)),
                    jsonPath("$.clouds.all", is(1)),
                    jsonPath("$.dt", is(1560350645)),
                    jsonPath("$.sys.type", is(1)),
                    jsonPath("$.sys.id", is(5122)),
                    jsonPath("$.sys.message", is(0.0139)),
                    jsonPath("$.sys.country", is("US")),
                    jsonPath("$.sys.sunrise", is(1560343627)),
                    jsonPath("$.sys.sunset", is(1560396563)),
                    jsonPath("$.timezone", is(-25200)),
                    jsonPath("$.id", is(420006353)),
                    jsonPath("$.name", is("Mountain View")),
                    jsonPath("$.cod", is(200))
            );
        }

    }

    @Nested
    class DeleteWeatherTest {

        @Test
        public void should_delete_a_weather_when_it_exists() throws Exception {
            mockMvc.perform(
                    delete("/weathers/{id}", 1)
            ).andExpectAll(
                    status().isNoContent()
            );
            verify(service, times(1)).delete(1L);
        }

        @Test
        public void should_return_an_error_when_weather_doesnt_exists() throws Exception {
            doThrow(new WeatherNotFoundException("There is no weather with id 2")).when(service).delete(2L);
            mockMvc.perform(
                    delete("/weathers/{id}", 2)
            ).andExpectAll(
                    status().isNotFound(),
                    jsonPath("$.message", is("There is no weather with id 2"))
            );
        }

    }

    @Nested
    class UpdateWeatherTest {

        @Test
        public void should_update_a_weather_when_it_exists() throws Exception {
            when(service.update(any(Weather.class))).then(returnsFirstArg());
            mockMvc.perform(
                    put("/weathers/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    	"coord": {
                                    		"lon": -122.08,
                                    		"lat": 37.39
                                    	},
                                    	"weather": [{
                                    		"id": 800,
                                    		"main": "Clear",
                                    		"description": "clear sky",
                                    		"icon": "01d"
                                    	}],
                                    	"base": "stations",
                                    	"main": {
                                    		"temp": 282.55,
                                    		"feels_like": 281.86,
                                    		"temp_min": 280.37,
                                    		"temp_max": 284.26,
                                    		"pressure": 1023,
                                    		"humidity": 100
                                    	},
                                    	"visibility": 16093,
                                    	"wind": {
                                    		"speed": 1.5,
                                    		"deg": 350
                                    	},
                                    	"clouds": {
                                    		"all": 1
                                    	},
                                    	"dt": 1560350645,
                                    	"sys": {
                                    		"type": 1,
                                    		"id": 5122,
                                    		"message": 0.0139,
                                    		"country": "US",
                                    		"sunrise": 1560343627,
                                    		"sunset": 1560396563
                                    	},
                                    	"timezone": -25200,
                                    	"id": 1,
                                    	"name": "Mountain View",
                                    	"cod": 200
                                    }
                                    """)
            ).andExpectAll(
                    status().isOk()
            );
        }

        @Test
        public void should_return_an_error_when_path_id_is_different_from_provided_weather_id() throws Exception {
            mockMvc.perform(
                    put("/weathers/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    	"coord": {
                                    		"lon": -122.08,
                                    		"lat": 37.39
                                    	},
                                    	"weather": [{
                                    		"id": 800,
                                    		"main": "Clear",
                                    		"description": "clear sky",
                                    		"icon": "01d"
                                    	}],
                                    	"base": "stations",
                                    	"main": {
                                    		"temp": 282.55,
                                    		"feels_like": 281.86,
                                    		"temp_min": 280.37,
                                    		"temp_max": 284.26,
                                    		"pressure": 1023,
                                    		"humidity": 100
                                    	},
                                    	"visibility": 16093,
                                    	"wind": {
                                    		"speed": 1.5,
                                    		"deg": 350
                                    	},
                                    	"clouds": {
                                    		"all": 1
                                    	},
                                    	"dt": 1560350645,
                                    	"sys": {
                                    		"type": 1,
                                    		"id": 5122,
                                    		"message": 0.0139,
                                    		"country": "US",
                                    		"sunrise": 1560343627,
                                    		"sunset": 1560396563
                                    	},
                                    	"timezone": -25200,
                                    	"id": 2,
                                    	"name": "Mountain View",
                                    	"cod": 200
                                    }
                                    """)
            ).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.message", is("Path id is different from the provided weather id"))
            );
        }

    }

}