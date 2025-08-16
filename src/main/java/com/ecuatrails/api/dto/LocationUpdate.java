package com.ecuatrails.api.dto;

public record LocationUpdate(Integer routeId, Double latitude, Double longitude, Double heading, Double speed,
		Long timestamp) {
}