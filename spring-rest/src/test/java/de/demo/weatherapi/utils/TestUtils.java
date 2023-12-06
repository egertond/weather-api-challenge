package de.demo.weatherapi.utils;

import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.dtos.SensorAverageResponse;
import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorHistoryResponse;
import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.entities.SensorAverageEntity;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.entities.SensorHistoryEntity;
import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.MetricEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class TestUtils {

    private TestUtils() {
        /* static class */
    }


    public static LocationRequest createLocationRequest() {
        return LocationRequest.builder()
                .name("test")
                .build();
    }

    public static LocationResponse createLocationResponse() {
        return LocationResponse.builder()
                .name("Test")
                .region("Region")
                .countryCode(CountryCodeEnum.IE)
                .timeZone(TimeZoneEnum.EUROPE_DUBLIN)
                .elevation(1D)
                .longitude(2D)
                .latitude(3D)
                .build();
    }

    public static SensorEntity createSensorEntity(UUID id) {
        return new SensorEntity(
                id,
                "Test",
                "Region",
                CountryCodeEnum.IE,
                TimeZoneEnum.EUROPE_DUBLIN,
                1D,
                2D,
                3D,
                LocalDateTime.now()
        );
    }

    public static SensorRequest createSensorRequest() {
        return SensorRequest.builder()
                .name("Test")
                .description("Region")
                .countryCode(CountryCodeEnum.IE)
                .timeZone(TimeZoneEnum.EUROPE_DUBLIN)
                .elevation(1D)
                .longitude(2D)
                .latitude(3D)
                .loadSensorData(true)
                .build();
    }

    public static SensorResponse createSensorResponse(UUID id, String name) {
        return SensorResponse.builder()
                .id(id)
                .name(name)
                .description(name + " region")
                .countryCode(CountryCodeEnum.IE)
                .timeZone(TimeZoneEnum.EUROPE_DUBLIN)
                .elevation(1D)
                .longitude(2D)
                .latitude(3D)
                .build();
    }

    public static SensorAverageEntity createSensorAverageEntity(String key) {
        return new SensorAverageEntity(
                key,
                1D, 2D, 3D,
                4D, 5D, 6D,
                7D, 8D, 9D,
                10, 11, 12,
                13D, 14D, 15D
        );
    }

    public static SensorHistoryEntity createSensorHistoryEntity(UUID id) {
        return new SensorHistoryEntity(
                UUID.randomUUID(),
                createSensorEntity(id),
                LocalDate.now(),
                1D,
                2D,
                LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 30, 45)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 15, 25)),
                5D,
                6D,
                7D,
                8,
                9D,
                LocalDateTime.now()
        );
    }

    public static SensorHistoryRequest createSensorHistoryRequest() {
        return SensorHistoryRequest.builder()
                .recordDate(LocalDate.now())
                .rainfallSum(1D)
                .snowfallSum(2D)
                .sunrise(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 30, 45)))
                .sunset(LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 15, 25)))
                .temperatureMin(5D)
                .temperatureMax(6D)
                .temperatureMean(7D)
                .windDirection(8)
                .windSpeedMax(9D)
                .build();
    }

    public static SensorHistoryResponse createSensorHistoryResponse(UUID id) {
        return SensorHistoryResponse.builder()
                .id(UUID.randomUUID())
                .sensor(SensorResponse.builder().id(id).build())
                .rainfallSum(1D)
                .snowfallSum(2D)
                .sunrise(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 30, 45)))
                .sunset(LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 15, 25)))
                .temperatureMin(5D)
                .temperatureMax(6D)
                .temperatureMean(7D)
                .windDirection(8)
                .windSpeedMax(9D)
                .build();
    }

    public static SensorAverageResponse createSensorAverageResponse(LocalDate startDate, LocalDate endDate, String key) {
        SensorAverageResponse res = SensorAverageResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
        res.addData(key, MetricEnum.RAINFALL, 1D, 2D, 3D);
        res.addData(key, MetricEnum.SNOWFALL, 3D, 4D, 5D);
        res.addData(key, MetricEnum.TEMPERATURE, 6D, 7D, 8D);
        res.addData(key, MetricEnum.WIND_DIRECTION, 9D, 10D, 11D);
        res.addData(key, MetricEnum.WIND_SPEED, 12D, 13D, 14D);
        return res;
    }

}
