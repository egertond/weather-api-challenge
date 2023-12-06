package de.demo.weatherapi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Custom Properties - manage additional configuration needed.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    private String locationApiUrl;

    private String weatherApiUrl;

    private Integer weatherLookbackDays;

}
