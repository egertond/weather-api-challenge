package de.demo.weatherapi.services.impl;

import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorHistoryResponse;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.entities.SensorHistoryEntity;
import de.demo.weatherapi.repositories.SensorHistoryRepository;
import de.demo.weatherapi.repositories.SensorRepository;
import de.demo.weatherapi.services.SensorHistoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Sensor History Service Impl - implements Sensor History Service.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorHistoryServiceImpl implements SensorHistoryService {

    final SensorRepository sensorRepository;
    final SensorHistoryRepository sensorHistoryRepository;

    /**
     * Create new Sensor History record.
     *
     * @param sensorId respective ID of the Sensor
     * @param request  sensor history details
     * @return confirmation of the newly created sensor history
     */
    @Override
    public SensorHistoryResponse create(UUID sensorId, SensorHistoryRequest request) {
        return sensorHistoryRepository.save(request.toEntity(findSensorById(sensorId))).toResponse();
    }

    /**
     * Find specific sensor history.
     *
     * @param sensorId respective ID of the Sensor
     * @return list of sensor history for the specified sensor
     */
    @Override
    public SensorHistoryResponse findById(UUID sensorId) {
        return sensorHistoryRepository.findById(sensorId)
                .map(SensorHistoryEntity::toResponse)
                .orElse(null);
    }

    /**
     * Find sensor history for the specified time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of sensor history for the time period
     */
    @Override
    public List<SensorHistoryResponse> findAll(LocalDate startDate, LocalDate endDate) {
        Streamable<SensorHistoryEntity> stream;
        if (startDate != null && endDate != null) {
            stream = Streamable.of(sensorHistoryRepository.findAllByDateRange(startDate, endDate));
        } else {
            stream = Streamable.of(sensorHistoryRepository.findAll());
        }
        return stream.map(SensorHistoryEntity::toResponse)
                .toList();
    }

    /**
     * For the specified sensor, get the history within the respective time period.
     *
     * @param sensorId  respective ID of the Sensor
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of sensor history for the time period
     */
    @Override
    public List<SensorHistoryResponse> findAllBySensor(UUID sensorId, LocalDate startDate, LocalDate endDate) {
        Streamable<SensorHistoryEntity> stream;
        if (startDate != null && endDate != null) {
            stream = Streamable.of(sensorHistoryRepository.findAllBySensorAndDateRange(sensorId, startDate, endDate));
        } else {
            stream = Streamable.of(sensorHistoryRepository.findAllBySensor(sensorId));
        }
        return stream.map(SensorHistoryEntity::toResponse)
                .toList();
    }

    /**
     * Remove all sensor history for the specific sensor.
     *
     * @param sensorId parent ID of the Sensor
     */
    @Override
    public void deleteForSensor(UUID sensorId) {
        sensorHistoryRepository.deleteBySensor(sensorId);
    }

    private SensorEntity findSensorById(UUID sensorId) {
        Optional<SensorEntity> sensor = sensorRepository.findById(sensorId);
        if (sensor.isEmpty()) {
            throw new EntityNotFoundException("Unable to find matching sensor record for " + sensorId);
        }
        return sensor.get();
    }

}
