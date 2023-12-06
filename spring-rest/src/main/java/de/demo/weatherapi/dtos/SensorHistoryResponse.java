package de.demo.weatherapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sensor History Response DTO - formatted sensor history returned to consumer.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
public record SensorHistoryResponse(
        UUID id,
        SensorResponse sensor,
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
}
