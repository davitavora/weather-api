package com.oddle.app.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Condition {

    private Integer id;
    private String main;
    private String description;
    private String icon;

}
