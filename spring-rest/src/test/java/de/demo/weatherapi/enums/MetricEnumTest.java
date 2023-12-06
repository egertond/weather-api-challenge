package de.demo.weatherapi.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MetricEnumTest {

    @Test
    void metric_toString() {
        assertEquals("Snowfall (cm)", MetricEnum.SNOWFALL.toString());
        assertEquals("Wind Speed (km/h)", MetricEnum.WIND_SPEED.toString());
    }

    @Test
    void metric_fromString() {
        assertEquals(MetricEnum.RAINFALL, MetricEnum.fromString("RainFall"));
        assertEquals(MetricEnum.WIND_DIRECTION, MetricEnum.fromString("wind_direction"));
    }

    @Test
    void metric_fromString_notFound() {
        assertNull(MetricEnum.fromString("BAD_CODE"));
    }
}
