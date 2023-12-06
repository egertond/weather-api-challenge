package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static de.demo.weatherapi.utils.TestUtils.createLocationRequest;
import static de.demo.weatherapi.utils.TestUtils.createSensorRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LookupServiceTest {

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    LookupService lookupService;

    @Test
    void getLocation_successful() {
        LocationRequest req = createLocationRequest();
        Map<String, ?> restResp = Map.of(
                "results", List.of(
                        Map.of(
                                "name", "Test",
                                "admin1", "Region",
                                "country_code", "IE",
                                "timezone", "UTC",
                                "elevation", 1D,
                                "longitude", 2D,
                                "latitude", 3D
                        )));

        when(restTemplate.exchange(
                any(URI.class), eq(HttpMethod.GET), isNull(), eq(new ParameterizedTypeReference<Map<String, ?>>() {
                }))
        ).thenReturn(new ResponseEntity<>(restResp, HttpStatus.OK));
        List<LocationResponse> res = lookupService.getLocationInfo(req);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("Test", res.get(0).name());
        assertEquals(3D, res.get(0).latitude());
    }

    @Test
    void getWeather_successful() {
        SensorRequest req = createSensorRequest();
        Map<String, ?> restResp = Map.of(
                "daily", Map.of(
                        "time", List.of("2023-11-01", "2023-11-02"),
                        "rain_sum", List.of(1D, 2D),
                        "snowfall_sum", List.of(3D, 4D),
                        "sunrise", List.of("2023-11-01T06:21", "2023-11-02T06:19"),
                        "sunset", List.of("2023-11-01T21:32", "2023-11-02T21:35"),
                        "temperature_2m_mean", List.of(5D, 6D),
                        "temperature_2m_min", List.of(7D, 8D),
                        "temperature_2m_max", List.of(9D, 10D),
                        "wind_direction_10m_dominant", List.of(11, 12),
                        "wind_speed_10m_max", List.of(13D, 14D)
                ));

        when(restTemplate.exchange(
                any(URI.class), eq(HttpMethod.GET), isNull(), eq(new ParameterizedTypeReference<Map<String, ?>>() {
                }))
        ).thenReturn(new ResponseEntity<>(restResp, HttpStatus.OK));
        List<SensorHistoryRequest> res = lookupService.getWeatherHistory(req);

        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(LocalDate.of(2023, 11, 1), res.get(0).recordDate());
        assertEquals(1D, res.get(0).rainfallSum());
        assertEquals(LocalDate.of(2023, 11, 2), res.get(1).recordDate());
        assertEquals(12, res.get(1).windDirection());
    }

}
