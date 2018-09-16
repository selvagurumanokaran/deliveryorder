package com.geeks.guru.dto.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeliveryOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private double distance;
	private DeliveryStatus status;

	public DeliveryOrder() {

	}

	public DeliveryOrder(double distance, DeliveryStatus status) {
		this.distance = distance;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public DeliveryStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryStatus status) {
		this.status = status;
	}

}
