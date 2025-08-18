package com.ecuatrails.api.dto;

public record AdminPoiTransportLinkDto(Integer id, Integer transportId, String transportName,
		Integer walkingDistanceMeters, Integer estimatedWalkingTime, String accessibilityNotes, Boolean status) {
}
