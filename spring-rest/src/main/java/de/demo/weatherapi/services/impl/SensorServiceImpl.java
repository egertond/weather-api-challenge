package de.demo.weatherapi.services.impl;

import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.repositories.SensorRepository;
import de.demo.weatherapi.services.LookupService;
import de.demo.weatherapi.services.SensorHistoryService;
import de.demo.weatherapi.services.SensorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Sensor Service Impl - implements Sensor Service.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorServiceImpl implements SensorService {

    final LookupService lookupService;
    final SensorHistoryService sensorHistoryService;
    final SensorRepository sensorRepository;

    /**
     * Create new Sensor record.
     *
     * @param request sensor details
     * @return confirmation of the newly created sensor
     */
    @Override
    public SensorResponse create(SensorRequest request) {
        SensorEntity entity = sensorRepository.save(request.toEntity());
        if (request.loadSensorData()) {
            for (SensorHistoryRequest history : lookupService.getWeatherHistory(request)) {
                sensorHistoryService.create(entity.getId(), history);
            }
        }
        return entity.toResponse();
    }

    /**
     * Get the configuration of a single Sensor.
     *
     * @param sensorId respective ID of the Sensor to find
     * @return configuration of the sensor
     */
    @Override
    public SensorResponse findById(UUID sensorId) {
        return sensorRepository.findById(sensorId)
                .map(SensorEntity::toResponse)
                .orElse(null);
    }

    /**
     * Find sensor by name.
     *
     * @param name name of the sensor to match
     * @return configuration of the matching sensor.
     */
    @Override
    public SensorResponse findByName(String name) {
        return sensorRepository.findByName(name)
                .map(SensorEntity::toResponse)
                .orElse(null);
    }

    /**
     * Find all Sensors that match the provided query.
     *
     * @param query filter used to match Sensors
     * @return list of all Sensors matching the filter
     */
    @Override
    public List<SensorResponse> findAll(String query) {
        return Streamable.of(sensorRepository.findAll())
                .filter(f -> StringUtils.isNotBlank(query) && StringUtils.containsIgnoreCase(f.getName(), query))
                .map(SensorEntity::toResponse)
                .toList();
    }

    /**
     * Update an existing sensor.
     *
     * @param sensorId respective ID of the Sensor to update
     * @param request  new Sensor configuration
     * @return confirmation of the update sensor
     */
    @Override
    public SensorResponse update(UUID sensorId, SensorRequest request) {
        Optional<SensorEntity> optional = sensorRepository.findById(sensorId);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Unable to fined matching record for " + sensorId);
        }
        SensorEntity entity = optional.get();
        entity.setElevation(request.elevation());
        entity.setLatitude(request.latitude());
        entity.setLongitude(request.longitude());
        return sensorRepository.save(entity).toResponse();
    }

    /**
     * Remove the specified.
     *
     * @param sensorId ID of the Sensor
     */
    @Override
    public void delete(UUID sensorId) {
        sensorRepository.deleteById(sensorId);
    }

}
