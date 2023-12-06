package de.demo.weatherapi.controllers;

import de.demo.weatherapi.dtos.SensorAverageResponse;
import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorHistoryResponse;
import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.services.SensorAverageService;
import de.demo.weatherapi.services.SensorHistoryService;
import de.demo.weatherapi.services.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller to handle all Sensor data queries.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@CrossOrigin
@RestController
@RequestMapping(path = "api/sensors")
@RequiredArgsConstructor
@Slf4j
public class SensorController {

    final SensorService sensorService;
    final SensorAverageService sensorAverageService;
    final SensorHistoryService sensorHistoryService;

    /**
     * Create a new Sensor.
     *
     * @param request sensor configuration
     * @return confirmation of the newly created sensor
     */
    @PostMapping()
    public ResponseEntity<SensorResponse> create(@RequestBody SensorRequest request) {
        try {
            return ResponseEntity.ok(sensorService.create(request));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Find all Sensors that match the provided query.
     *
     * @param query filter used to match Sensors
     * @return list of all Sensors matching the filter
     */
    @GetMapping()
    public ResponseEntity<List<SensorResponse>> readAll(@RequestParam String query) {
        try {
            return ResponseEntity.ok(sensorService.findAll(query));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Averaged data (for all Sensors) over the respective time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of average Sensor data, grouped by the Sensors
     */
    @GetMapping(path = "averages")
    public ResponseEntity<SensorAverageResponse> readAverageHistory(@RequestParam LocalDate startDate, LocalDate endDate) {
        try {
            return ResponseEntity.ok(sensorAverageService.findAverage(startDate, endDate));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * History extract (for all Sensors) over the respective time period.
     *
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list containing the filtered Sensor history records
     */
    @GetMapping(path = "history")
    public ResponseEntity<List<SensorHistoryResponse>> readAllHistory(@RequestParam LocalDate startDate, LocalDate endDate) {
        try {
            return ResponseEntity.ok(sensorHistoryService.findAll(startDate, endDate));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Update an existing sensor.
     *
     * @param sensorId respective ID of the Sensor to update
     * @param request  new Sensor configuration
     * @return confirmation of the update sensor
     */
    @PostMapping(path = "{sensorId}")
    public ResponseEntity<SensorResponse> update(@PathVariable UUID sensorId, @RequestBody SensorRequest request) {
        try {
            return ResponseEntity.ok(sensorService.update(sensorId, request));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get the configuration of a single Sensor.
     *
     * @param sensorId respective ID of the Sensor to find
     * @return configuration of the sensor
     */
    @GetMapping(path = "{sensorId}")
    public ResponseEntity<SensorResponse> read(@PathVariable UUID sensorId) {
        try {
            return ResponseEntity.ok(sensorService.findById(sensorId));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Averaged data (for specific Sensor) over the respective time period.
     *
     * @param sensorId  respective ID of the Sensor to find
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list of average Sensor data, grouped by month.
     */
    @GetMapping(path = "{sensorId}/averages")
    public ResponseEntity<SensorAverageResponse> readSensorAverageHistory(@PathVariable UUID sensorId, @RequestParam LocalDate startDate, LocalDate endDate) {
        try {
            return ResponseEntity.ok(sensorAverageService.findAverageBySensor(sensorId, startDate, endDate));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * History extract (for specific Sensors) over the respective time period.
     *
     * @param sensorId  respective ID of the Sensor to find
     * @param startDate start of the time period filter
     * @param endDate   end of the time period filter
     * @return list containing the filtered Sensor history records
     */
    @GetMapping(path = "{sensorId}/history")
    public ResponseEntity<List<SensorHistoryResponse>> readSensorAllHistory(@PathVariable UUID sensorId, @RequestParam LocalDate startDate, LocalDate endDate) {
        try {
            return ResponseEntity.ok(sensorHistoryService.findAllBySensor(sensorId, startDate, endDate));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Add new Sensor History record.
     *
     * @param sensorId respective ID of the Sensor
     * @param request  sensor history details
     * @return confirmation of the newly created sensor history
     */
    @PostMapping(path = "{sensorId}/history")
    public ResponseEntity<SensorHistoryResponse> addSensorHistory(@PathVariable UUID sensorId, @RequestBody SensorHistoryRequest request) {
        try {
            return ResponseEntity.ok(sensorHistoryService.create(sensorId, request));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
