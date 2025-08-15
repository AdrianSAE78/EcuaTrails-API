package com.ecuatrails.api.dto;

public record InterestPointTransportDto(
	Integer relationId, Integer interestPointId, String interestPointName,
	Integer transportId, String transportName,
	Integer walkingDistanceMeters, Integer estimatedWalkingTime, String accessibilityNotes
) {}
