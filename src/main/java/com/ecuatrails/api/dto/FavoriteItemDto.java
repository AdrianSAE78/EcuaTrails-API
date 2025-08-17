package com.ecuatrails.api.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FavoriteItemDto", description = "Elemento de favoritos del usuario")
public record FavoriteItemDto(@Schema(example = "12") Integer favoriteId, @Schema(example = "205") Integer routeId,
		@Schema(example = "Mirador del Cóndor") String routeName,
		@Schema(description = "Dificultad (EASY, MEDIUM, HARD)", example = "MEDIUM") String difficulty,
		@Schema(description = "Duración estimada en formato ISO-8601", example = "PT4H") Duration estimatedDuration,
		@Schema(description = "Fecha de creación (ISO-8601)", example = "2025-08-15T09:05:31") LocalDateTime created) {
}
