package de.demo.weatherapi.repositories;

import de.demo.weatherapi.entities.SensorAverageEntity;
import de.demo.weatherapi.entities.SensorHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Sensor Average Repository - JPA repository to map queries for averaged sensor history metrics.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface SensorAverageRepository extends JpaRepository<SensorHistoryEntity, UUID> {

    /**
     * Find Sensor history averages within the respective time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of averaged sensor history grouped by the sensors
     */
    @Query("""
             SELECT
                new de.demo.weatherapi.entities.SensorAverageEntity(
                    CAST(h.sensor.id AS STRING),
                    ROUND(AVG(h.rainfallSum), 2),
                    ROUND(MIN(h.rainfallSum), 2),
                    ROUND(MAX(h.rainfallSum), 2),
                    ROUND(AVG(h.snowfallSum), 2),
                    ROUND(MIN(h.snowfallSum), 2),
                    ROUND(MAX(h.snowfallSum), 2),
                    ROUND(AVG(h.temperatureMean), 2),
                    ROUND(MIN(h.temperatureMin), 2),
                    ROUND(MAX(h.temperatureMax), 2),
                    CAST(AVG(h.windDirection) AS INTEGER),
                    CAST(MIN(h.windDirection) AS INTEGER),
                    CAST(MAX(h.windDirection) AS INTEGER),
                    ROUND(AVG(h.windSpeedMax), 2),
                    ROUND(MIN(h.windSpeedMax), 2),
                    ROUND(MAX(h.windSpeedMax), 2)
                )
               FROM SensorHistoryEntity h
              WHERE h.recordDate BETWEEN :startDate AND :endDate
              GROUP BY h.sensor.id
            """)
    List<SensorAverageEntity> findAverageInDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * For the specified sensor, get the history averages within the respective time period.
     *
     * @param sensorId  respective ID of the Sensor
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of averaged sensor history grouped by month
     */
    @Query("""
             SELECT
                new de.demo.weatherapi.entities.SensorAverageEntity(
                    CAST(TO_CHAR(h.recordDate,'yyyy-MM') AS STRING),
                    ROUND(AVG(h.rainfallSum), 2),
                    ROUND(MIN(h.rainfallSum), 2),
                    ROUND(MAX(h.rainfallSum), 2),
                    ROUND(AVG(h.snowfallSum), 2),
                    ROUND(MIN(h.snowfallSum), 2),
                    ROUND(MAX(h.snowfallSum), 2),
                    ROUND(AVG(h.temperatureMean), 2),
                    ROUND(MIN(h.temperatureMin), 2),
                    ROUND(MAX(h.temperatureMax), 2),
                    CAST(AVG(h.windDirection) AS INTEGER),
                    CAST(MIN(h.windDirection) AS INTEGER),
                    CAST(MAX(h.windDirection) AS INTEGER),
                    ROUND(AVG(h.windSpeedMax), 2),
                    ROUND(MIN(h.windSpeedMax), 2),
                    ROUND(MAX(h.windSpeedMax), 2)
                )
               FROM SensorHistoryEntity h
              WHERE h.sensor.id = :sensorId
                AND h.recordDate BETWEEN :startDate AND :endDate
              GROUP BY CAST(TO_CHAR(h.recordDate,'yyyy-MM') AS STRING)
            """)
    List<SensorAverageEntity> findSensorAverageInDateRange(UUID sensorId, LocalDate startDate, LocalDate endDate);

}
