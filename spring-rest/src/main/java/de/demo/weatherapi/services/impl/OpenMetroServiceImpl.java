package de.demo.weatherapi.services.impl;

import de.demo.weatherapi.CustomProperties;
import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;
import de.demo.weatherapi.services.LookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * OpenMetro Service Impl - Implementation of the Lookup Service.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenMetroServiceImpl implements LookupService {

    final CustomProperties customProperties;
    final RestTemplate restTemplate;

    private final List<String> searchKeys = List.of(
            "temperature_2m_max", "temperature_2m_min", "temperature_2m_mean", "sunrise,sunset", "rain_sum",
            "snowfall_sum", "wind_speed_10m_max", "wind_direction_10m_dominant"
    );

    /**
     * Get Location information to support the sensor creation.
     *
     * @param request search filter to find locations
     * @return list of matched locations
     */
    @Override
    public List<LocationResponse> getLocationInfo(LocationRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(customProperties.getLocationApiUrl())
                .queryParam("name", request.name())
                .queryParam("count", "10")
                .queryParam("language", "en")
                .queryParam("format", "json")
                .build().toUri();
        Map<String, ?> response = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<String, ?>>() {
                        })
                .getBody();
        return parseLocationData(response);
    }

    /**
     * Get Weather data to support sensor history creation.
     *
     * @param request parent sensor request details
     * @return list of new sensor history data
     */
    @Override
    public List<SensorHistoryRequest> getWeatherHistory(SensorRequest request) {
        LocalDate endDate = LocalDate.now().minusDays(5);
        LocalDate startDate = endDate.minusDays(customProperties.getWeatherLookbackDays());
        URI uri = UriComponentsBuilder.fromUriString(customProperties.getWeatherApiUrl())
                .queryParam("latitude", request.latitude())
                .queryParam("longitude", request.longitude())
                .queryParam("start_date", startDate.format(DateTimeFormatter.ISO_DATE))
                .queryParam("end_date", endDate.format(DateTimeFormatter.ISO_DATE))
                .queryParam("daily", String.join(",", searchKeys))
                .build().toUri();
        Map<String, ?> response = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<String, ?>>() {
                        })
                .getBody();
        return parseWeatherData(response);
    }

    private List<LocationResponse> parseLocationData(Map<String, ?> response) {
        if (response != null && response.get("results") instanceof List) {
            List<LocationResponse> locations = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<Map<String, ?>> results = (List<Map<String, ?>>) response.get("results");
            for (Map<String, ?> location : results) {
                String name = (String) location.get("name");
                String region = (String) location.get("admin1");
                locations.add(
                        LocationResponse.builder()
                                .name(name)
                                .region(region)
                                .countryCode(CountryCodeEnum.fromString((String) location.get("country_code")))
                                .timeZone(TimeZoneEnum.fromString((String) location.get("timezone")))
                                .elevation((Double) location.get("elevation"))
                                .longitude((Double) location.get("longitude"))
                                .latitude((Double) location.get("latitude"))
                                .build()
                );
            }
            return locations;
        }
        return Collections.emptyList();
    }

    private List<SensorHistoryRequest> parseWeatherData(Map<String, ?> response) {
        if (response != null && response.get("daily") instanceof Map) {
            List<SensorHistoryRequest> history = new ArrayList<>();
            @SuppressWarnings("unchecked")
            Map<String, ?> daily = (Map<String, ?>) response.get("daily");
            @SuppressWarnings("unchecked")
            List<String> dates = (List<String>) daily.get("time");
            @SuppressWarnings("unchecked")
            List<Double> rainfallSum = (List<Double>) daily.get("rain_sum");
            @SuppressWarnings("unchecked")
            List<Double> snowfallSum = (List<Double>) daily.get("snowfall_sum");
            @SuppressWarnings("unchecked")
            List<String> sunrise = (List<String>) daily.get("sunrise");
            @SuppressWarnings("unchecked")
            List<String> sunset = (List<String>) daily.get("sunset");
            @SuppressWarnings("unchecked")
            List<Double> temperatureMean = (List<Double>) daily.get("temperature_2m_mean");
            @SuppressWarnings("unchecked")
            List<Double> temperatureMin = (List<Double>) daily.get("temperature_2m_min");
            @SuppressWarnings("unchecked")
            List<Double> temperatureMax = (List<Double>) daily.get("temperature_2m_max");
            @SuppressWarnings("unchecked")
            List<Integer> windDirection = (List<Integer>) daily.get("wind_direction_10m_dominant");
            @SuppressWarnings("unchecked")
            List<Double> windSpeedMax = (List<Double>) daily.get("wind_speed_10m_max");
            for (int x = 0; x < dates.size(); x++) {
                history.add(SensorHistoryRequest.builder()
                        .recordDate(LocalDate.parse(dates.get(x)))
                        .rainfallSum(rainfallSum.get(x))
                        .snowfallSum(snowfallSum.get(x))
                        .sunrise(LocalDateTime.parse(sunrise.get(x)))
                        .sunset(LocalDateTime.parse(sunset.get(x)))
                        .temperatureMean(temperatureMean.get(x))
                        .temperatureMin(temperatureMin.get(x))
                        .temperatureMax(temperatureMax.get(x))
                        .windDirection(windDirection.get(x))
                        .windSpeedMax(windSpeedMax.get(x))
                        .build());
            }
            return history;
        }
        return Collections.emptyList();
    }

}
