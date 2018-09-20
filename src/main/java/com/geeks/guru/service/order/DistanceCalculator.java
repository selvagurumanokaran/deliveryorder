package com.geeks.guru.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;

@Component
public class DistanceCalculator {

    private GeoApiContext context;

    @Autowired
    public DistanceCalculator(@Value("${com.geeks.guru.google.apikey}") String apiKey) {
	this.context = new GeoApiContext().setApiKey(apiKey);
    }

    public long calculateDistance(Double[] origin, Double[] destination) throws Exception {
	String[] origins = { origin[0] + "," + origin[1] };
	String[] destinations = { destination[0] + "," + destination[1] };
	DistanceMatrix matrixResult = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();
	DistanceMatrixElementStatus status = matrixResult.rows[0].elements[0].status;
	if (status == DistanceMatrixElementStatus.OK) {
	    return matrixResult.rows[0].elements[0].distance.inMeters;
	}
	return -1;
    }
}
