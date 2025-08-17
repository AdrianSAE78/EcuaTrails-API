package com.ecuatrails.api.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MeStatsDto", description = "Estadísticas agregadas del usuario")
public record MeStatsDto(@Schema(description = "Total de rutas finalizadas", example = "18") long totalFinished,
		@Schema(description = "Distancia total (km)", example = "124.6") float totalDistanceKm,
		@Schema(description = "Duración total (minutos)", example = "980") long totalDurationMinutes,
		@Schema(description = "Última actividad (ISO-8601)", example = "2025-08-14T18:05:00") LocalDateTime lastActivityAt) {
}
