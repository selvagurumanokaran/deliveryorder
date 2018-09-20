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
    private long distance;
    private DeliveryStatus status;

    public DeliveryOrder() {

    }

    public DeliveryOrder(long distance, DeliveryStatus status) {
	this.distance = distance;
	this.status = status;
    }

    public DeliveryOrder(int id, long distance, DeliveryStatus status) {
	this.id = id;
	this.distance = distance;
	this.status = status;
    }

    public int getId() {
	return id;
    }

    public long getDistance() {
	return distance;
    }

    public DeliveryStatus getStatus() {
	return status;
    }

}
