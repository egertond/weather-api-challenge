package de.demo.weatherapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sensor Average Entity - mapped resulted from the average history query.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorAverageEntity {
    private String key;
    private Double rainfallMean;
    private Double rainfallMin;
    private Double rainfallMax;
    private Double snowfallMean;
    private Double snowfallMin;
    private Double snowfallMax;
    private Double temperatureMean;
    private Double temperatureMin;
    private Double temperatureMax;
    private Integer windDirectionMean;
    private Integer windDirectionMin;
    private Integer windDirectionMax;
    private Double windSpeedMean;
    private Double windSpeedMin;
    private Double windSpeedMax;

}
