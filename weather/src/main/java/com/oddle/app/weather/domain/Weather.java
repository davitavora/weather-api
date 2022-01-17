package com.oddle.app.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private Coord coord;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "weather_condition",
            joinColumns = @JoinColumn(
                    name = "weather_id",
                    foreignKey = @ForeignKey(name = "fk_weather")
            )
    )
    private List<Condition> conditions;

    private String base;

    @Embedded
    private Main main;

    private Integer visibility;

    @Embedded
    private Wind wind;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "all", column = @Column(name = "clouds_all")),
    })
    private Clouds clouds;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oneHour", column = @Column(name = "snow_one_hour")),
            @AttributeOverride(name = "threeHour", column = @Column(name = "snow_three_hour"))
    })
    private Fallout snow;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oneHour", column = @Column(name = "rain_one_hour")),
            @AttributeOverride(name = "threeHour", column = @Column(name = "rain_three_hour"))
    })
    private Fallout rain;

    private Long dt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "sys_id"))
    })
    private Sys sys;

    private Integer timezone;

    private String name;

    private Integer cod;

    public Weather(Long id) {
        this.id = id;
    }

}
