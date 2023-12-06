package de.demo.weatherapi.dtos;

import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;
import lombok.Builder;

import java.util.UUID;

/**
 * Sensor Response DTO - formatted sensor returned to consumer.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
public record SensorResponse(
        UUID id,
        String name,
        String description,
        CountryCodeEnum countryCode,
        TimeZoneEnum timeZone,
        Double elevation,
        Double longitude,
        Double latitude
) {
}
