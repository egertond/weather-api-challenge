package de.demo.weatherapi.dtos;

import lombok.Builder;

/**
 * Location Request DTO - used by the location lookup controller.
 *
 * @author Dwight Egerton
 * @since 0.0.1
 */
@Builder
public record LocationRequest(String name) {
}
