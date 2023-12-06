package de.demo.weatherapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.entities.SensorHistoryEntity;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sensor History Request DTO - used when adding new sensor history.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
public record SensorHistoryRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
        LocalDate recordDate,
        Double rainfallSum,
        Double snowfallSum,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
        LocalDateTime sunrise,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
        LocalDateTime sunset,
        Double temperatureMean,
        Double temperatureMin,
        Double temperatureMax,
        Integer windDirection,
        Double windSpeedMax
) {

    /**
     * Conversion function to map a history request to history entity.
     *
     * @param parent link to the respective parent sensor entity
     * @return new sensor history entity
     */
    public SensorHistoryEntity toEntity(SensorEntity parent) {
        return SensorHistoryEntity.builder()
                .id(UUID.randomUUID())
                .sensor(parent)
                .recordDate(recordDate)
                .rainfallSum(rainfallSum == null ? 0 : rainfallSum)
                .snowfallSum(snowfallSum == null ? 0 : snowfallSum)
                .sunrise(sunrise)
                .sunset(sunset)
                .temperatureMean(temperatureMean == null ? 0 : temperatureMean)
                .temperatureMin(temperatureMin == null ? 0 : temperatureMin)
                .temperatureMax(temperatureMax == null ? 0 : temperatureMax)
                .windDirection(windDirection == null ? 0 : windDirection)
                .windSpeedMax(windSpeedMax == null ? 0 : windSpeedMax)
                .modifiedDateTime(LocalDateTime.now())
                .build();
    }

}
