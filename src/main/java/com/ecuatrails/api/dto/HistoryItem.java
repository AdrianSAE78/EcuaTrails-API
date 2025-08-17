package com.ecuatrails.api.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "HistoryItem", description = "Elemento del historial de rutas")
public record HistoryItem(@Schema(example = "31") Integer historyId, @Schema(example = "205") Integer routeId,
		@Schema(example = "Mirador del Cóndor") String routeName,
		@Schema(description = "Dificultad (EASY, MEDIUM, HARD)", example = "MEDIUM") String difficulty,
		@Schema(description = "Duración estimada (ISO-8601)", example = "PT4H") Duration estimatedDuration,
		@Schema(description = "Fecha/hora de la ruta (ISO-8601)", example = "2025-08-12T10:15:00") LocalDateTime routeDate,
		@Schema(description = "Marca si la ruta fue completada", example = "true") Boolean isFinished) {
}
