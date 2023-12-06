package de.demo.weatherapi.services;

import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.entities.SensorEntity;
import de.demo.weatherapi.repositories.SensorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.GenericJDBCException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.demo.weatherapi.utils.TestUtils.createSensorEntity;
import static de.demo.weatherapi.utils.TestUtils.createSensorHistoryRequest;
import static de.demo.weatherapi.utils.TestUtils.createSensorRequest;
import static de.demo.weatherapi.utils.TestUtils.createSensorResponse;
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
public class SensorServiceTest {

    @MockBean
    LookupService lookupService;

    @MockBean
    SensorHistoryService sensorHistoryService;
    @MockBean
    SensorRepository sensorRepository;

    @Autowired
    SensorService sensorService;

    @Test
    void createSensor_successful() {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();
        SensorEntity entity = createSensorEntity(id);
        SensorHistoryRequest hist = createSensorHistoryRequest();

        given(lookupService.getWeatherHistory(any()))
                .willReturn(List.of(hist));
        given(sensorRepository.save(any()))
                .willReturn(entity);
        SensorResponse res = sensorService.create(req);

        assertNotNull(res);
        assertEquals(id, res.id());
        assertEquals(req.name(), res.name());
        assertEquals(req.countryCode(), res.countryCode());
        assertEquals(req.longitude(), res.longitude());
        verify(sensorHistoryService, times(1)).create(eq(id), any());
    }

    @Test
    void createSensor_unsuccessful() {
        SensorRequest req = createSensorRequest();

        given(sensorRepository.save(any()))
                .willThrow(new GenericJDBCException("Failed", new SQLException()));
        SensorResponse res;
        try {
            res = sensorService.create(req);
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

    @Test
    void findSensor_successful() {
        UUID id = UUID.randomUUID();
        SensorEntity entity = createSensorEntity(id);

        // find by ID
        given(sensorRepository.findById(eq(id)))
                .willReturn(Optional.of(entity));
        SensorResponse res1 = sensorService.findById(id);

        assertNotNull(res1);
        assertEquals(id, res1.id());

        // find by NAME
        given(sensorRepository.findByName(eq("test")))
                .willReturn(Optional.of(entity));
        SensorResponse res2 = sensorService.findByName("test");

        assertNotNull(res2);
        assertEquals(id, res2.id());
    }

    @Test
    void findSensor_unsuccessful() {
        UUID id = UUID.randomUUID();

        // find by ID
        given(sensorRepository.findById(eq(id)))
                .willReturn(null);
        SensorResponse res1;
        try {
            res1 = sensorService.findById(id);
        } catch (Exception ex) {
            res1 = null;
        }

        assertNull(res1);

        // find by NAME
        given(sensorRepository.findByName(eq("test")))
                .willReturn(null);
        SensorResponse res2;
        try {
            res2 = sensorService.findByName("test");
        } catch (Exception ex) {
            res2 = null;
        }

        assertNull(res2);
    }

    @Test
    void findAll_successful() {
        UUID id = UUID.randomUUID();
        SensorEntity entity = createSensorEntity(id);

        // with matching query
        given(sensorRepository.findAll())
                .willReturn(List.of(entity));
        List<SensorResponse> res1 = sensorService.findAll("test");

        assertNotNull(res1);
        assertEquals(1, res1.size());

        // with bad query
        List<SensorResponse> res2 = sensorService.findAll("bad");

        assertNotNull(res2);
        assertEquals(0, res2.size());
    }

    @Test
    void findAll_unsuccessful() {
        UUID id = UUID.randomUUID();

        // find by ID
        given(sensorRepository.findAll())
                .willReturn(null);
        List<SensorResponse> res1;
        try {
            res1 = sensorService.findAll("test");
        } catch (Exception ex) {
            res1 = null;
        }

        assertNull(res1);
    }

    @Test
    void updateSensor_successful() {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();
        SensorEntity entity = createSensorEntity(id);

        given(sensorRepository.findById(eq(id)))
                .willReturn(Optional.of(entity));
        given(sensorRepository.save(any()))
                .willReturn(entity);
        SensorResponse res = sensorService.update(id, req);

        assertNotNull(res);
        assertEquals(id, res.id());
        assertEquals(req.elevation(), res.elevation());
        assertEquals(req.longitude(), res.longitude());
        assertEquals(req.latitude(), res.latitude());
    }

    @Test
    void updateSensor_unsuccessful() {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();

        given(sensorRepository.findById(eq(id)))
                .willReturn(Optional.empty());
        SensorResponse res;
        try {
            res = sensorService.update(id, req);
        } catch (EntityNotFoundException ex) {
            res = null;
        }

        assertNull(res);
    }

    @Test
    void deleteSensor_successful() {
        UUID id = UUID.randomUUID();

        sensorService.delete(id);

        verify(sensorRepository, times(1)).deleteById(eq(id));
    }

    @Test
    void deleteSensor_unsuccessful() {
        UUID id = UUID.randomUUID();

        doThrow(new GenericJDBCException("Failed", new SQLException()))
                .when(sensorRepository).deleteById(eq(id));
        SensorResponse res;
        try {
            sensorService.delete(id);
            res = createSensorResponse(id, "Test");
        } catch (Exception ex) {
            res = null;
        }

        assertNull(res);
    }

}
