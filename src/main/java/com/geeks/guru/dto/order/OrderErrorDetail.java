package com.geeks.guru.dto.order;

public class OrderErrorDetail {

    private String error;

    public OrderErrorDetail() {

    }

    public OrderErrorDetail(String error) {
	this.error = error;
    }

    public String getError() {
	return error;
    }
}
