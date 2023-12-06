package de.demo.weatherapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Metric ENUM - utility enum to map sensor metrics.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Getter
public enum MetricEnum {

    RAINFALL("Rainfall (mm)"),
    SNOWFALL("Snowfall (cm)"),
    TEMPERATURE("Temperature (°C)"),
    WIND_DIRECTION("Wind Direction (°)"),
    WIND_SPEED("Wind Speed (km/h)");

    private final String label;

    MetricEnum(String label) {
        this.label = label;
    }

    public static MetricEnum fromString(String value) {
        if (value != null) {
            for (MetricEnum t : values()) {
                if (value.equalsIgnoreCase(t.name())) return t;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return getLabel();
    }

}
