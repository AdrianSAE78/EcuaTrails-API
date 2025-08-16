package com.ecuatrails.api.dto;

import java.time.Duration;

public record AdminRouteDetailDto(Integer id, String name, String description, Integer categoryId,
		Duration estimatedDuration, String recommendedSchedule, Float distance, String difficulty, Boolean status) {
}