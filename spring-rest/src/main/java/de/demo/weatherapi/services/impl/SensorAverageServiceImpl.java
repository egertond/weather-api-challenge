package de.demo.weatherapi.services.impl;

import de.demo.weatherapi.dtos.SensorAverageResponse;
import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.entities.SensorAverageEntity;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.enums.MetricEnum;
import de.demo.weatherapi.repositories.SensorAverageRepository;
import de.demo.weatherapi.repositories.SensorRepository;
import de.demo.weatherapi.services.SensorAverageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Sensor Average Service Impl - implements Sensor Average Service.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAverageServiceImpl implements SensorAverageService {

    final SensorRepository sensorRepository;
    final SensorAverageRepository sensorAverageRepository;

    /**
     * Find Sensor history averages within the respective time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return averaged sensor history grouped by the sensors
     */
    @Override
    public SensorAverageResponse findAverage(LocalDate startDate, LocalDate endDate) {
        SensorAverageResponse response = SensorAverageResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
        for (SensorAverageEntity entity : sensorAverageRepository.findAverageInDateRange(startDate, endDate)) {
            SensorResponse sensor = findSensorById(UUID.fromString(entity.getKey())).toResponse();
            response.addData(sensor.name(), MetricEnum.RAINFALL, entity.getRainfallMean(), entity.getRainfallMin(), entity.getRainfallMax());
            response.addData(sensor.name(), MetricEnum.SNOWFALL, entity.getSnowfallMean(), entity.getSnowfallMin(), entity.getSnowfallMax());
            response.addData(sensor.name(), MetricEnum.TEMPERATURE, entity.getTemperatureMean(), entity.getTemperatureMin(), entity.getTemperatureMax());
            response.addData(sensor.name(), MetricEnum.WIND_DIRECTION, entity.getWindDirectionMean(), entity.getWindDirectionMin(), entity.getWindDirectionMax());
            response.addData(sensor.name(), MetricEnum.WIND_SPEED, entity.getWindSpeedMean(), entity.getWindSpeedMin(), entity.getWindSpeedMax());
        }
        return response;
    }

    /**
     * For the specified sensor, get the history averages within the respective time period.
     *
     * @param sensorId  respective ID of the Sensor
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return averaged sensor history grouped by month
     */
    @Override
    public SensorAverageResponse findAverageBySensor(UUID sensorId, LocalDate startDate, LocalDate endDate) {
        SensorAverageResponse response = SensorAverageResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
        for (SensorAverageEntity entity : sensorAverageRepository.findSensorAverageInDateRange(sensorId, startDate, endDate)) {
            response.addData(entity.getKey(), MetricEnum.RAINFALL, entity.getRainfallMean(), entity.getRainfallMin(), entity.getRainfallMax());
            response.addData(entity.getKey(), MetricEnum.SNOWFALL, entity.getSnowfallMean(), entity.getSnowfallMin(), entity.getSnowfallMax());
            response.addData(entity.getKey(), MetricEnum.TEMPERATURE, entity.getTemperatureMean(), entity.getTemperatureMin(), entity.getTemperatureMax());
            response.addData(entity.getKey(), MetricEnum.WIND_DIRECTION, entity.getWindDirectionMean(), entity.getWindDirectionMin(), entity.getWindDirectionMax());
            response.addData(entity.getKey(), MetricEnum.WIND_SPEED, entity.getWindSpeedMean(), entity.getWindSpeedMin(), entity.getWindSpeedMax());
        }
        return response;
    }

    private SensorEntity findSensorById(UUID sensorId) {
        Optional<SensorEntity> sensor = sensorRepository.findById(sensorId);
        if (sensor.isEmpty()) {
            throw new EntityNotFoundException("Unable to find matching sensor record for " + sensorId);
        }
        return sensor.get();
    }

}
