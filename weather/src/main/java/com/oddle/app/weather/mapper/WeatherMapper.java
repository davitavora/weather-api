package com.oddle.app.weather.mapper;

import com.oddle.app.weather.domain.*;
import com.oddle.app.weather.model.dto.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherDTO toDTO(Weather weather);

    ConditionDTO toDTO(Condition weather);

    CoordDTO toDTO(Coord coord);

    MainDTO toDTO(Main main);

    WindDTO toDTO(Wind wind);

    CloudsDTO toDTO(Clouds clouds);

    FalloutDTO toDTO(Fallout fallout);

    SysDTO toDTO(Sys sys);

    List<WeatherDTO> toDTO(List<Weather> weathers);

    Weather toEntity(WeatherDTO weather);

    Condition toEntity(ConditionDTO weather);

    Coord toEntity(CoordDTO coord);

    Main toEntity(MainDTO main);

    Wind toEntity(WindDTO wind);

    Clouds toEntity(CloudsDTO clouds);

    Fallout toEntity(FalloutDTO fallout);

    Sys toEntity(SysDTO sys);

}
