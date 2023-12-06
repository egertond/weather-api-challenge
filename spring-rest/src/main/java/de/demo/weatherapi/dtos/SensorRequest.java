package de.demo.weatherapi.dtos;

import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sensor Request DTO - used when adding new sensor.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
public record SensorRequest(
        String name,
        String description,
        CountryCodeEnum countryCode,
        TimeZoneEnum timeZone,
        Double elevation,
        Double longitude,
        Double latitude,
        Boolean loadSensorData
) {

    /**
     * Conversion function to map a sensor request to sensor entity.
     *
     * @return new sensor entity
     */
    public SensorEntity toEntity() {
        return SensorEntity.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .countryCode(countryCode)
                .timeZone(timeZone)
                .elevation(elevation)
                .longitude(longitude)
                .latitude(latitude)
                .modifiedDateTime(LocalDateTime.now())
                .build();
    }

}
