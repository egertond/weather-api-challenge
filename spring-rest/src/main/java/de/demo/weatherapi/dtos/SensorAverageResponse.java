package de.demo.weatherapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.demo.weatherapi.enums.MetricEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sensor Average Response DTO - to return averaged sensor history.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
@Data
public class SensorAverageResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate endDate;

    @Builder.Default
    private Map<String, List<AverageData<?>>> data = new HashMap<>();

    public <T> void addData(String key, MetricEnum metric, T meanValue, T minValue, T maxValue) {
        if (!data.containsKey(key)) data.put(key, new ArrayList<>());
        data.get(key).add(new AverageData<>(metric, meanValue, minValue, maxValue));
    }

    record AverageData<T>(
            MetricEnum metric,
            T meanValue,
            T minValue,
            T maxValue
    ) {
    }

}