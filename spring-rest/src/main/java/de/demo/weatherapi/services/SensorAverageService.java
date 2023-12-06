package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.SensorAverageResponse;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Sensor Average Service - manage queries to get average sensor history
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface SensorAverageService {

    /**
     * Find Sensor history averages within the respective time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return averaged sensor history grouped by the sensors
     */
    SensorAverageResponse findAverage(LocalDate startDate, LocalDate endDate);

    /**
     * For the specified sensor, get the history averages within the respective time period.
     *
     * @param sensorId  respective ID of the Sensor
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return averaged sensor history grouped by month
     */
    SensorAverageResponse findAverageBySensor(UUID sensorId, LocalDate startDate, LocalDate endDate);

}
