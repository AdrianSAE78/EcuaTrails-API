package com.ecuatrails.api.dto;

import java.time.Duration;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RouteListItem", description = "Item resumido de ruta")
public record RouteListItem(@Schema(example = "205") Integer id, @Schema(example = "Mirador del Cóndor") String name,
		@Schema(example = "Ascenso con vistas panorámicas") String description,
		@Schema(description = "Nombre de categoría legible", example = "Senderismo") String category,
		@Schema(description = "Duración estimada (ISO-8601)", example = "PT4H") Duration estimatedDuration,
		@Schema(description = "Distancia total (km)", example = "12.1") Float distance,
		@Schema(description = "Dificultad (EASY, MEDIUM, HARD)", example = "MEDIUM") String difficulty) {
}
