package de.demo.weatherapi.controllers;

import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;
import de.demo.weatherapi.services.LookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Controller to handle miscellaneous/supporting data queries.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@CrossOrigin
@RestController
@RequestMapping(path = "api/lookup")
@RequiredArgsConstructor
@Slf4j
public class LookupController {

    final LookupService lookupService;

    /**
     * Utility endpoint to return a simple map of country code and name.
     *
     * @return country key/value pairs
     */
    @GetMapping(path = "countries")
    public ResponseEntity<Map<String, String>> getCountries() {
        TreeMap<String, String> response = new TreeMap<>();
        for (CountryCodeEnum c : CountryCodeEnum.values()) {
            response.put(c.name(), c.getCountry());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Utility endpoint to return a simple map of time-zone code and descriptor.
     *
     * @return time-zone key/value pairs
     */
    @GetMapping(path = "timezones")
    public ResponseEntity<Map<String, String>> getTimeZones() {
        TreeMap<String, String> response = new TreeMap<>();
        for (TimeZoneEnum t : TimeZoneEnum.values()) {
            response.put(t.name(), t.toString());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Utility endpoint to return a list of locations that matched the provided query.
     *
     * @param request location query request
     * @return list of location found matching the respective query
     */
    @PostMapping(path = "locations")
    public ResponseEntity<List<LocationResponse>> getLocations(@RequestBody LocationRequest request) {
        try {
            return ResponseEntity.ok(lookupService.getLocationInfo(request));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
