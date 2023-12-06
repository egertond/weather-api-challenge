package de.demo.weatherapi.entities;

import de.demo.weatherapi.dtos.SensorHistoryResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sensor History Entity - storage of the respective sensor history data.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SensorHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sensor_id", nullable = false, updatable = false)
    private SensorEntity sensor;
    @Column(nullable = false)
    private LocalDate recordDate;
    @Builder.Default
    @Column(nullable = false)
    private Double rainfallSum = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Double snowfallSum = 0D;
    @Column(nullable = false)
    private LocalDateTime sunrise;
    @Column(nullable = false)
    private LocalDateTime sunset;
    @Builder.Default
    @Column(nullable = false)
    private Double temperatureMean = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Double temperatureMin = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Double temperatureMax = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Integer windDirection = 0;
    @Builder.Default
    @Column(nullable = false)
    private Double windSpeedMax = 0D;
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

    /**
     * Conversion function to map a history entity to history response.
     *
     * @return new sensor history response
     */
    public SensorHistoryResponse toResponse() {
        return SensorHistoryResponse.builder()
                .id(id)
                .sensor(sensor.toResponse())
                .recordDate(recordDate)
                .rainfallSum(rainfallSum)
                .snowfallSum(snowfallSum)
                .sunrise(sunrise)
                .sunset(sunset)
                .temperatureMean(temperatureMean)
                .temperatureMin(temperatureMin)
                .temperatureMax(temperatureMax)
                .windDirection(windDirection)
                .windSpeedMax(windSpeedMax)
                .build();
    }
}
