package com.ecuatrails.api.dto;

public record UpdatePoiTransportLinkRequest(Integer walkingDistanceMeters, Integer estimatedWalkingTime,
		String accessibilityNotes, Boolean status) {
}
