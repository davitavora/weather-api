package com.oddle.app.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Wind {

    private Double speed;
    private Double deg;
    private Double gust;

}
