package com.geeks.guru.service.order;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

	public static double distance(double startLatitude, double startLongitude, double endLatitude,
			double endLongitude) {
		double theta = startLongitude - endLongitude;
		double dist = Math.sin(degree2radian(startLatitude)) * Math.sin(degree2radian(endLatitude))
				+ Math.cos(degree2radian(startLatitude)) * Math.cos(degree2radian(endLatitude))
						* Math.cos(degree2radian(theta));
		dist = Math.acos(dist);
		dist = radian2degree(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return dist;
	}

	private static double degree2radian(double degree) {
		return (degree * Math.PI / 180.0);
	}

	private static double radian2degree(double radian) {
		return (radian * 180 / Math.PI);
	}
}
