package com.ecuatrails.api.dto;

public record Directions(
	double originLat, double originLng,
	double destLat, double destLng,
	String mode,
	double distanceMeters,
	int etaSeconds,
	java.util.List<double[]> line
) {}
