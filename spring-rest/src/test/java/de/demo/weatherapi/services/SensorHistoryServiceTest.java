package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorHistoryResponse;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.entities.SensorHistoryEntity;
import de.demo.weatherapi.repositories.SensorHistoryRepository;
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

import static de.demo.weatherapi.utils.TestUtils.createSensorEntity;
import static de.demo.weatherapi.utils.TestUtils.createSensorHistoryEntity;
import static de.demo.weatherapi.utils.TestUtils.createSensorHistoryRequest;
import static de.demo.weatherapi.utils.TestUtils.createSensorHistoryResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SensorHistoryServiceTest {

    @MockBean
    SensorRepository sensorRepository;

    @MockBean
    SensorHistoryRepository sensorHistoryRepository;

    @Autowired
    SensorHistoryService sensorHistoryService;

    @Test
    void createHistory_successful() {
        UUID id = UUID.randomUUID();
        SensorHistoryRequest req = createSensorHistoryRequest();
        SensorEntity entity1 = createSensorEntity(id);
        SensorHistoryEntity entity2 = createSensorHistoryEntity(id);

        given(sensorRepository.findById(eq(id)))
                .willReturn(Optional.of(entity1));
        given(sensorHistoryRepository.save(any()))
                .willReturn(entity2);
        SensorHistoryResponse res = sensorHistoryService.create(id, req);

        assertNotNull(res);
        assertEquals(id, res.sensor().id());
        assertEquals(req.rainfallSum(), res.rainfallSum());
        assertEquals(req.sunrise(), res.sunrise());
        assertEquals(req.windSpeedMax(), res.windSpeedMax());
    }

    @Test
    void createHistory_unsuccessful() {
        UUID id = UUID.randomUUID();
        SensorHistoryRequest req = createSensorHistoryRequest();
        SensorEntity entity = createSensorEntity(id);

        given(sensorRepository.findById(eq(id)))
                .willReturn(Optional.empty());
        SensorHistoryResponse res;
        try {
            res = sensorHistoryService.create(id, req);
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

    @Test
    void findById_successful() {
        UUID id = UUID.randomUUID();
        SensorHistoryEntity entity = createSensorHistoryEntity(id);

        given(sensorHistoryRepository.findById(eq(id)))
                .willReturn(Optional.of(entity));
        SensorHistoryResponse res = sensorHistoryService.findById(id);

        assertNotNull(res);
        assertEquals(id, res.sensor().id());
    }

    @Test
    void findById_unsuccessful() {
        UUID id = UUID.randomUUID();

        given(sensorHistoryRepository.findById(eq(id)))
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        SensorHistoryResponse res;
        try {
            res = sensorHistoryService.findById(id);
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

    @Test
    void findAll_successful() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        SensorHistoryEntity entity1 = createSensorHistoryEntity(UUID.randomUUID());
        SensorHistoryEntity entity2 = createSensorHistoryEntity(UUID.randomUUID());

        // find all results
        given(sensorHistoryRepository.findAll())
                .willReturn(List.of(entity1, entity2));
        List<SensorHistoryResponse> res1 = sensorHistoryService.findAll(null, null);

        assertNotNull(res1);
        assertEquals(2, res1.size());

        // with date range
        given(sensorHistoryRepository.findAllByDateRange(eq(startDate), eq(endDate)))
                .willReturn(List.of(entity1));
        List<SensorHistoryResponse> res2 = sensorHistoryService.findAll(startDate, endDate);

        assertNotNull(res2);
        assertEquals(1, res2.size());
    }

    @Test
    void findAll_unsuccessful() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        // find all results
        given(sensorHistoryRepository.findAll())
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        List<SensorHistoryResponse> res1;
        try {
            res1 = sensorHistoryService.findAll(null, null);
        } catch (Exception ex) {
            res1 = null;
        }

        assertNull(res1);

        // with date range
        given(sensorHistoryRepository.findAllByDateRange(eq(startDate), eq(endDate)))
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        List<SensorHistoryResponse> res2;
        try {
            res2 = sensorHistoryService.findAll(startDate, endDate);
        } catch (Exception ex) {
            res2 = null;
        }

        assertNull(res2);
    }

    @Test
    void findAllBySensor_successful() {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        SensorHistoryEntity entity1 = createSensorHistoryEntity(UUID.randomUUID());
        SensorHistoryEntity entity2 = createSensorHistoryEntity(UUID.randomUUID());

        // find all results
        given(sensorHistoryRepository.findAllBySensor(eq(id)))
                .willReturn(List.of(entity1, entity2));
        List<SensorHistoryResponse> res1 = sensorHistoryService.findAllBySensor(id, null, null);

        assertNotNull(res1);
        assertEquals(2, res1.size());

        // with date range
        given(sensorHistoryRepository.findAllBySensorAndDateRange(eq(id), eq(startDate), eq(endDate)))
                .willReturn(List.of(entity1));
        List<SensorHistoryResponse> res2 = sensorHistoryService.findAllBySensor(id, startDate, endDate);

        assertNotNull(res2);
        assertEquals(1, res2.size());
    }

    @Test
    void findAllBySensor_unsuccessful() {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        // find all results
        given(sensorHistoryRepository.findAllBySensor(eq(id)))
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        List<SensorHistoryResponse> res1;
        try {
            res1 = sensorHistoryService.findAllBySensor(id, null, null);
        } catch (Exception ex) {
            res1 = null;
        }

        assertNull(res1);

        // with date range
        given(sensorHistoryRepository.findAllBySensorAndDateRange(eq(id), eq(startDate), eq(endDate)))
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        List<SensorHistoryResponse> res2;
        try {
            res2 = sensorHistoryService.findAllBySensor(id, startDate, endDate);
        } catch (Exception ex) {
            res2 = null;
        }

        assertNull(res2);
    }

    @Test
    void deleteForSensor_successful() {
        UUID id = UUID.randomUUID();

        sensorHistoryService.deleteForSensor(id);

        verify(sensorHistoryRepository, times(1)).deleteBySensor(eq(id));
    }

    @Test
    void deleteForSensor_unsuccessful() {
        UUID id = UUID.randomUUID();

        doThrow(new GenericJDBCException("Failed", new SQLException()))
                .when(sensorHistoryRepository).deleteBySensor(eq(id));
        SensorHistoryResponse res;
        try {
            sensorHistoryService.deleteForSensor(id);
            res = createSensorHistoryResponse(id);
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

}
