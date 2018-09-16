package com.geeks.guru.dto.order;

public class OrderException {

	String error;

	public OrderException(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
