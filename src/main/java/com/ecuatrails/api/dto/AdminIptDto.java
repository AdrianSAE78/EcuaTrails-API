package com.ecuatrails.api.dto;

public record AdminIptDto(Integer id, Integer interestPointId, String interestPointName, Integer transportId,
		String transportName, Integer walkingDistanceMeters, Integer estimatedWalkingTime, String accessibilityNotes,
		Boolean status) {
}
