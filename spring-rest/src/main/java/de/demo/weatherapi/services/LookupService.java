package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorRequest;

import java.util.List;

/**
 * Lookup Service - query location and weather data from third-party.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
public interface LookupService {

    /**
     * Get Location information to support the sensor creation.
     *
     * @param request search filter to find locations
     * @return list of matched locations
     */
    List<LocationResponse> getLocationInfo(LocationRequest request);

    /**
     * Get Weather data to support sensor history creation.
     *
     * @param request parent sensor request details
     * @return list of new sensor history data
     */
    List<SensorHistoryRequest> getWeatherHistory(SensorRequest request);

}
