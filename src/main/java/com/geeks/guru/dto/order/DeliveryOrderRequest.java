package com.geeks.guru.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeliveryOrderRequest {

    @NotNull(message = "Origin cannot be empty.")
    @Size(min = 2, max = 2, message = "Origin must contain exactly one Latitude and one Longtitude.")
    private Double[] origin;

    @NotNull(message = "Destination cannot be empty.")
    @Size(min = 2, max = 2, message = "Destination must contain exactly one Latitude and one Longtitude.")
    private Double[] destination;

    public Double[] getOrigin() {
	return origin;
    }

    public Double[] getDestination() {
	return destination;
    }

}
