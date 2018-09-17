package com.geeks.guru.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DeliveryOrderStatus {

    @NotNull(message = "Status cannot be empty.")
    @Pattern(regexp = "taken", message = "Status can only be 'taken'.")
    private String status;

    public DeliveryOrderStatus() {

    }

    public DeliveryOrderStatus(String status) {
	this.status = status;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

}
