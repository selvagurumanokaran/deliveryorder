package com.geeks.guru.service.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;

@Component
public class DistanceCalculator {

    @Value("${com.geeks.guru.google.apikey}")
    private String apiKey;

    public String calculateDistance(Double[] origin, Double[] destination) throws NullPointerException, Exception {
	GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
	String[] origins = { origin[0] + "," + origin[1] };
	String[] destinations = { destination[0] + "," + destination[1] };
	DistanceMatrix matrixResult = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();
	return matrixResult.rows[0].elements[0].distance.humanReadable;
    }
}
