package de.demo.weatherapi.entities;

import de.demo.weatherapi.dtos.SensorResponse;
import de.demo.weatherapi.enums.CountryCodeEnum;
import de.demo.weatherapi.enums.TimeZoneEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sensor Entity - storage of the respective sensor data.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SensorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, updatable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCodeEnum countryCode;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeZoneEnum timeZone;
    @Column(nullable = false)
    private Double elevation;
    @Column(nullable = false)
    private Double longitude;
    @Column(nullable = false)
    private Double latitude;
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

    /**
     * Conversion function to map a sensor entity to sensor response.
     *
     * @return new sensor response
     */
    public SensorResponse toResponse() {
        return SensorResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .countryCode(countryCode)
                .timeZone(timeZone)
                .elevation(elevation)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
