package de.demo.weatherapi.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeZoneEnumTest {

    @Test
    void timeZone_toString() {
        assertEquals("GMT", TimeZoneEnum.GMT.toString());
        assertEquals("America/New_York", TimeZoneEnum.AMERICA_NEW_YORK.toString());
    }

    @Test
    void timeZone_fromString() {
        assertEquals(TimeZoneEnum.UTC, TimeZoneEnum.fromString("utc"));
        assertEquals(TimeZoneEnum.EUROPE_ROME, TimeZoneEnum.fromString("Europe/Rome"));
    }

    @Test
    void timeZone_fromString_notFound_default() {
        assertEquals(TimeZoneEnum.DEFAULT, TimeZoneEnum.fromString("BAD_CODE"));
        assertEquals(TimeZoneEnum.DEFAULT, TimeZoneEnum.fromString("Invalid/Country"));
    }
}
