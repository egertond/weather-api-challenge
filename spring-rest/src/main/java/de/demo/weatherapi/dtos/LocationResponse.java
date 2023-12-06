package de.demo.weatherapi.dtos;

import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

/**
 * Location Response DTO - used by the location lookup controller.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
public record LocationResponse(
        String name,
        String region,
        CountryCodeEnum countryCode,
        TimeZoneEnum timeZone,
        Double elevation,
        Double longitude,
        Double latitude
) {

    /**
     * Conversion function to map a location response to a Sensor request.
     *
     * @return new sensor request object
     */
    public SensorRequest toSensorRequest() {
        return SensorRequest.builder()
                .name(name())
                .description((StringUtils.isBlank(region()) ? "" : region() + ", ") + countryCode())
                .countryCode(countryCode())
                .timeZone(timeZone())
                .elevation(elevation())
                .longitude(longitude())
                .latitude(latitude())
                .loadSensorData(true)
                .build();
    }
}
