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
    private String distance;
    private DeliveryStatus status;

    public DeliveryOrder() {

    }

    public DeliveryOrder(String distance, DeliveryStatus status) {
	this.distance = distance;
	this.status = status;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getDistance() {
	return distance;
    }

    public void setDistance(String distance) {
	this.distance = distance;
    }

    public DeliveryStatus getStatus() {
	return status;
    }

    public void setStatus(DeliveryStatus status) {
	this.status = status;
    }

}
