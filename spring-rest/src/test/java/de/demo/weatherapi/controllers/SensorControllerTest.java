package de.demo.weatherapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.demo.weatherapi.dtos.SensorAverageResponse;
import de.demo.weatherapi.dtos.SensorHistoryRequest;
import de.demo.weatherapi.dtos.SensorHistoryResponse;
import de.demo.weatherapi.dtos.SensorRequest;
import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.services.SensorAverageService;
import de.demo.weatherapi.services.SensorHistoryService;
import de.demo.weatherapi.services.SensorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static de.demo.weatherapi.utils.TestUtils.createSensorAverageResponse;
import static de.demo.weatherapi.utils.TestUtils.createSensorHistoryRequest;
import static de.demo.weatherapi.utils.TestUtils.createSensorHistoryResponse;
import static de.demo.weatherapi.utils.TestUtils.createSensorRequest;
import static de.demo.weatherapi.utils.TestUtils.createSensorResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SensorController.class)
public class SensorControllerTest {

    @MockBean
    private SensorService sensorService;

    @MockBean
    private SensorAverageService sensorAverageService;

    @MockBean
    private SensorHistoryService sensorHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc sensorController;

    @Test
    void createSensor_returns_200() throws Exception {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();
        SensorResponse res = createSensorResponse(id, "Test");

        given(sensorService.create(any()))
                .willReturn(res);
        sensorController.perform(post("/api/sensors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(res)));
    }

    @Test
    void createSensor_returns_500() throws Exception {
        SensorRequest req = createSensorRequest();

        given(sensorService.create(any()))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(post("/api/sensors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readAll_returns_200() throws Exception {
        SensorResponse res1 = createSensorResponse(UUID.randomUUID(), "Test1");
        SensorResponse res2 = createSensorResponse(UUID.randomUUID(), "Test2");

        given(sensorService.findAll(any()))
                .willReturn(List.of(res1, res2));
        sensorController.perform(get("/api/sensors?query=test")
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(res1, res2))));
    }

    @Test
    void readAll_returns_500() throws Exception {
        given(sensorService.findAll(any()))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(get("/api/sensors?query=test")
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readAverges_returns_200() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);
        SensorAverageResponse res = createSensorAverageResponse(startDate, endDate, "Test");

        given(sensorAverageService.findAverage(eq(startDate), eq(endDate)))
                .willReturn(res);
        sensorController.perform(get("/api/sensors/averages?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(res)));
    }

    @Test
    void readAverges_returns_500() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);

        given(sensorAverageService.findAverage(eq(startDate), eq(endDate)))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(get("/api/sensors/averages?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readHistory_returns_200() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);
        SensorHistoryResponse res1 = createSensorHistoryResponse(UUID.randomUUID());
        SensorHistoryResponse res2 = createSensorHistoryResponse(UUID.randomUUID());

        given(sensorHistoryService.findAll(eq(startDate), eq(endDate)))
                .willReturn(List.of(res1, res2));
        sensorController.perform(get("/api/sensors/history?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(res1, res2))));
    }

    @Test
    void readHistory_returns_500() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);

        given(sensorHistoryService.findAll(eq(startDate), eq(endDate)))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(get("/api/sensors/history?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateSensor_returns_200() throws Exception {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();
        SensorResponse res = createSensorResponse(id, "Test");

        given(sensorService.update(eq(id), any()))
                .willReturn(res);
        sensorController.perform(post("/api/sensors/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(res)));
    }

    @Test
    void updateSensor_returns_500() throws Exception {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();

        given(sensorService.update(eq(id), any()))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(post("/api/sensors/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readSensor_returns_200() throws Exception {
        UUID id = UUID.randomUUID();
        SensorResponse res = createSensorResponse(id, "Test");

        given(sensorService.findById(eq(id)))
                .willReturn(res);
        sensorController.perform(get("/api/sensors/" + id)
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(res)));
    }

    @Test
    void readSensor_returns_500() throws Exception {
        UUID id = UUID.randomUUID();

        given(sensorService.findById(any()))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(get("/api/sensors/" + id)
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readSensorAverges_returns_200() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);
        SensorAverageResponse res = createSensorAverageResponse(startDate, endDate, "2023-11");

        given(sensorAverageService.findAverageBySensor(eq(id), eq(startDate), eq(endDate)))
                .willReturn(res);
        sensorController.perform(get("/api/sensors/" + id + "/averages?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(res)));
    }

    @Test
    void readSensorAverges_returns_500() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);

        given(sensorAverageService.findAverageBySensor(eq(id), eq(startDate), eq(endDate)))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(get("/api/sensors/" + id + "/averages?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readSensorHistory_returns_200() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);
        SensorHistoryResponse res1 = createSensorHistoryResponse(UUID.randomUUID());
        SensorHistoryResponse res2 = createSensorHistoryResponse(UUID.randomUUID());

        given(sensorHistoryService.findAllBySensor(eq(id), eq(startDate), eq(endDate)))
                .willReturn(List.of(res1, res2));
        sensorController.perform(get("/api/sensors/" + id + "/history?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(res1, res2))));
    }

    @Test
    void readSensorHistory_returns_500() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 30);

        given(sensorHistoryService.findAllBySensor(eq(id), eq(startDate), eq(endDate)))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(get("/api/sensors/" + id + "/history?startDate=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&endDate=" + endDate.format(DateTimeFormatter.ISO_DATE))
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addHistory_returns_200() throws Exception {
        UUID id = UUID.randomUUID();
        SensorHistoryRequest req = createSensorHistoryRequest();
        SensorHistoryResponse res = createSensorHistoryResponse(id);

        given(sensorHistoryService.create(eq(id), any()))
                .willReturn(res);
        sensorController.perform(post("/api/sensors/" + id + "/history")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(res)));
    }

    @Test
    void addHistory_returns_500() throws Exception {
        UUID id = UUID.randomUUID();
        SensorRequest req = createSensorRequest();

        given(sensorHistoryService.create(eq(id), any()))
                .willThrow(new IllegalArgumentException("Some Error"));
        sensorController.perform(post("/api/sensors/" + id + "/history")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isInternalServerError());
    }

}
