package com.ecuatrails.api.dto;

public record CreateIptLinkRequest(Integer transportId, Integer walkingDistanceMeters, Integer estimatedWalkingTime,
		String accessibilityNotes, Boolean status) {
}
