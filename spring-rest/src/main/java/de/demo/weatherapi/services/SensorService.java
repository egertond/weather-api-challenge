package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.dtos.SensorResponse;

import java.util.List;
import java.util.UUID;

/**
 * Sensor Service - manage queries to get sensor data.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface SensorService {

    /**
     * Create new Sensor record.
     *
     * @param request sensor details
     * @return confirmation of the newly created sensor
     */
    SensorResponse create(SensorRequest request);

    /**
     * Get the configuration of a single Sensor.
     *
     * @param sensorId respective ID of the Sensor to find
     * @return configuration of the sensor
     */
    SensorResponse findById(UUID sensorId);

    /**
     * Find sensor by name.
     *
     * @param name name of the sensor to match
     * @return configuration of the matching sensor.
     */
    SensorResponse findByName(String name);

    /**
     * Find all Sensors that match the provided query.
     *
     * @param query filter used to match Sensors
     * @return list of all Sensors matching the filter
     */
    List<SensorResponse> findAll(String query);

    /**
     * Update an existing sensor.
     *
     * @param sensorId respective ID of the Sensor to update
     * @param request  new Sensor configuration
     * @return confirmation of the update sensor
     */
    SensorResponse update(UUID sensorId, SensorRequest request);

    /**
     * Remove the specified.
     *
     * @param sensorId ID of the Sensor
     */
    void delete(UUID sensorId);

}
