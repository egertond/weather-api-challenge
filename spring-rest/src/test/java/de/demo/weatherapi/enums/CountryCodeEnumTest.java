package de.demo.weatherapi.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class CountryCodeEnumTest {

    @Test
    void countryCode_toString() {
        assertEquals("IE", CountryCodeEnum.IE.toString());
        assertEquals("US", CountryCodeEnum.US.toString());
    }

    @Test
    void countryCode_fromString() {
        assertEquals(CountryCodeEnum.DE, CountryCodeEnum.fromString("DE"));
        assertEquals(CountryCodeEnum.CA, CountryCodeEnum.fromString("Canada"));
    }

    @Test
    void countryCode_fromString_notFound() {
        assertNull(CountryCodeEnum.fromString("BAD_CODE"));
        assertNull(CountryCodeEnum.fromString("Invalid Country"));
    }
}
