package de.demo.weatherapi.repositories;

import de.demo.weatherapi.entities.SensorHistoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Sensor History Repository - CRUD repository persist sensor history.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface SensorHistoryRepository extends CrudRepository<SensorHistoryEntity, UUID> {

    /**
     * Custom query to find sensor history for the specified time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of sensor history for the time period
     */
    @Query("SELECT h FROM SensorHistoryEntity h WHERE h.recordDate BETWEEN :startDate AND :endDate ORDER BY h.recordDate DESC")
    List<SensorHistoryEntity> findAllByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Custom query to find specific sensor history.
     *
     * @param sensorId respective ID of the Sensor
     * @return list of sensor history for the specified sensor
     */
    @Query("SELECT h FROM SensorHistoryEntity h WHERE h.sensor.id = :sensorId ORDER BY h.recordDate DESC")
    List<SensorHistoryEntity> findAllBySensor(UUID sensorId);

    /**
     * For the specified sensor, get the history within the respective time period.
     *
     * @param sensorId  respective ID of the Sensor
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of sensor history for the time period
     */
    @Query("SELECT h FROM SensorHistoryEntity h WHERE h.sensor.id = :sensorId AND h.recordDate BETWEEN :startDate AND :endDate ORDER BY h.recordDate DESC")
    List<SensorHistoryEntity> findAllBySensorAndDateRange(UUID sensorId, LocalDate startDate, LocalDate endDate);

    /**
     * Remove all sensor history for the specific sensor.
     *
     * @param sensorId parent ID of the Sensor
     */
    @Modifying
    @Query("DELETE FROM SensorHistoryEntity h WHERE h.sensor.id = :sensorId")
    void deleteBySensor(UUID sensorId);

}
