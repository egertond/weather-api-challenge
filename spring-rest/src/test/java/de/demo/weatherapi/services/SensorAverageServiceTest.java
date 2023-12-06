package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.SensorAverageResponse;
import de.demo.weatherapi.entities.SensorAverageEntity;
import de.demo.weatherapi.repositories.SensorAverageRepository;
import de.demo.weatherapi.repositories.SensorRepository;
import org.hibernate.exception.GenericJDBCException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.demo.weatherapi.utils.TestUtils.createSensorAverageEntity;
import static de.demo.weatherapi.utils.TestUtils.createSensorEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SensorAverageServiceTest {

    @MockBean
    SensorRepository sensorRepository;

    @MockBean
    SensorAverageRepository sensorAverageRepository;

    @Autowired
    SensorAverageService sensorAverageService;


    @Test
    void findAverage_successful() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        SensorAverageEntity entity = createSensorAverageEntity(UUID.randomUUID().toString());

        given(sensorRepository.findById(any()))
                .willReturn(Optional.of(createSensorEntity(UUID.randomUUID())));
        given(sensorAverageRepository.findAverageInDateRange(eq(startDate), eq(endDate)))
                .willReturn(List.of(entity));
        SensorAverageResponse res = sensorAverageService.findAverage(startDate, endDate);

        assertNotNull(res);
        assertEquals(startDate, res.getStartDate());
        assertEquals(endDate, res.getEndDate());
        assertEquals(1, res.getData().size());
    }

    @Test
    void findAverage_unsuccessful() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        SensorAverageEntity entity = createSensorAverageEntity(UUID.randomUUID().toString());

        given(sensorRepository.findById(any()))
                .willReturn(Optional.empty());
        given(sensorAverageRepository.findAverageInDateRange(eq(startDate), eq(endDate)))
                .willReturn(List.of(entity));
        SensorAverageResponse res;
        try {
            res = sensorAverageService.findAverage(startDate, endDate);
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

    @Test
    void findAverageBySensor_successful() {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        SensorAverageEntity entity1 = createSensorAverageEntity("2023-09");
        SensorAverageEntity entity2 = createSensorAverageEntity("2023-10");

        given(sensorAverageRepository.findSensorAverageInDateRange(eq(id), eq(startDate), eq(endDate)))
                .willReturn(List.of(entity1, entity2));
        SensorAverageResponse res = sensorAverageService.findAverageBySensor(id, startDate, endDate);

        assertNotNull(res);
        assertEquals(startDate, res.getStartDate());
        assertEquals(endDate, res.getEndDate());
        assertEquals(2, res.getData().size());
    }

    @Test
    void findAverageBySensor_unsuccessful() {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        given(sensorAverageRepository.findSensorAverageInDateRange(eq(id), eq(startDate), eq(endDate)))
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        SensorAverageResponse res;
        try {
            res = sensorAverageService.findAverageBySensor(id, startDate, endDate);
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

}
