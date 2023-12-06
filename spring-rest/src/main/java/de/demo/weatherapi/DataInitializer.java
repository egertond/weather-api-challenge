package de.demo.weatherapi;

import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.services.LookupService;
import de.demo.weatherapi.services.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Data Initializer - on start up will initialise the database with some initial data.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    final LookupService lookupService;
    final SensorService sensorService;

    private final List<String> sensors = List.of("Cork", "Dublin", "Galway", "Letterkenny");

    @Override
    public void run(ApplicationArguments args) {
        for (String name : sensors) {
            if (sensorService.findByName(name) == null) {
                List<LocationResponse> locations = lookupService.getLocationInfo(new LocationRequest(name));
                if (!locations.isEmpty()) {
                    sensorService.create(locations.get(0).toSensorRequest());
                }
            }
        }
        log.info("done data initialisation...");
    }

}
