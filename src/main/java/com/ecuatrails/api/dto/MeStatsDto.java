package com.ecuatrails.api.dto;

public record MeStatsDto(
	long totalFinished,
	float totalDistanceKm,
	long totalDurationMinutes,
	java.time.LocalDateTime lastActivityAt
) {}
