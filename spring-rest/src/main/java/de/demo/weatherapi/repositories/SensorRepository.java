package de.demo.weatherapi.repositories;

import de.demo.weatherapi.entities.SensorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Sensor Repository - CRUD repository persist sensor data.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface SensorRepository extends CrudRepository<SensorEntity, UUID> {

    /**
     * Custom query to find sensor by name.
     *
     * @param name name of the sensor to match
     * @return configuration of the matching sensor.
     */
    Optional<SensorEntity> findByName(String name);

}
