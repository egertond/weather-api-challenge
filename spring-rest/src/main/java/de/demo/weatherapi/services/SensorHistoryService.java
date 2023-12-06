package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Sensor History Service - manage queries to get sensor history
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface SensorHistoryService {

    /**
     * Create new Sensor History record.
     *
     * @param sensorId respective ID of the Sensor
     * @param request  sensor history details
     * @return confirmation of the newly created sensor history
     */
    SensorHistoryResponse create(UUID sensorId, SensorHistoryRequest request);

    /**
     * Find specific sensor history.
     *
     * @param sensorId respective ID of the Sensor
     * @return list of sensor history for the specified sensor
     */
    SensorHistoryResponse findById(UUID sensorId);

    /**
     * Find sensor history for the specified time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of sensor history for the time period
     */
    List<SensorHistoryResponse> findAll(LocalDate startDate, LocalDate endDate);

    /**
     * For the specified sensor, get the history within the respective time period.
     *
     * @param sensorId  respective ID of the Sensor
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of sensor history for the time period
     */
    List<SensorHistoryResponse> findAllBySensor(UUID sensorId, LocalDate startDate, LocalDate endDate);

    /**
     * Remove all sensor history for the specific sensor.
     *
     * @param sensorId parent ID of the Sensor
     */
    void deleteForSensor(UUID sensorId);

}
