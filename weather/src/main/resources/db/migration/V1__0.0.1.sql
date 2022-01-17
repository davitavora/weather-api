CREATE TABLE weather
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    base            VARCHAR(255),
    clouds_all      INTEGER,
    cod             INTEGER,
    lat             DOUBLE,
    lon             DOUBLE,
    dt              BIGINT,
    feels_like      DOUBLE,
    grnd_level      INTEGER,
    humidity        INTEGER,
    pressure        INTEGER,
    sea_level       INTEGER,
    temp            DOUBLE,
    temp_max        DOUBLE,
    temp_min        DOUBLE,
    name            VARCHAR(255),
    rain_one_hour   DOUBLE,
    rain_three_hour DOUBLE,
    snow_one_hour   DOUBLE,
    snow_three_hour DOUBLE,
    country         VARCHAR(255),
    sys_id          INTEGER,
    message         DOUBLE,
    sunrise         BIGINT,
    sunset          BIGINT,
    type            INTEGER,
    timezone        INTEGER,
    visibility      INTEGER,
    deg             DOUBLE,
    gust            DOUBLE,
    speed           DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE weather_condition
(
    weather_id  BIGINT NOT NULL,
    description VARCHAR(255),
    icon        VARCHAR(255),
    id          INTEGER,
    main        VARCHAR(255),
    CONSTRAINT FOREIGN KEY fk_weather (weather_id) REFERENCES weather (id)
)