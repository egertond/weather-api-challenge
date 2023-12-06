package de.demo.weatherapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.demo.weatherapi.dtos.LocationRequest;
import de.demo.weatherapi.dtos.LocationResponse;
import de.demo.weatherapi.services.LookupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static de.demo.weatherapi.utils.TestUtils.createLocationRequest;
import static de.demo.weatherapi.utils.TestUtils.createLocationResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LookupController.class)
public class LookupControllerTest {

    @MockBean
    private LookupService lookupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc lookupController;

    @Test
    void getCountries_returns_200() throws Exception {
        lookupController.perform(get("/api/lookup/countries")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void getTimezones_returns_200() throws Exception {
        lookupController.perform(get("/api/lookup/timezones")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void getLocations_returns_200() throws Exception {
        LocationRequest req = createLocationRequest();
        LocationResponse res = createLocationResponse();

        given(lookupService.getLocationInfo(any()))
                .willReturn(List.of(res));
        lookupController.perform(post("/api/lookup/locations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(res))));
    }

    @Test
    void getLocations_returns_500() throws Exception {
        LocationRequest req = createLocationRequest();

        given(lookupService.getLocationInfo(any()))
                .willThrow(new RestClientException("Some Error"));
        lookupController.perform(post("/api/lookup/locations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isInternalServerError());
    }

}
