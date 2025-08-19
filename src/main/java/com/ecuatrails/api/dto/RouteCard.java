package com.ecuatrails.api.dto;

import java.time.Duration;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RouteCard", description = "Resumen de ruta para tarjetas del dashboard")
public record RouteCard(@Schema(example = "101") Integer id, @Schema(example = "Laguna Encantada") String name,
		@Schema(example = "Circuito alrededor de la laguna") String description,
		@Schema(description = "Nombre de categoría legible", example = "Senderismo") String category,
		@Schema(description = "Imágenes del alojamiento") List<ImageDto> images,
		@Schema(description = "Duración estimada (ISO-8601)", example = "PT2H30M") Duration estimatedDuration,
		@Schema(description = "Distancia total en kilómetros", example = "7.8") Float distance,
		@Schema(description = "Dificultad (ej. EASY, MEDIUM, HARD)", example = "EASY") String difficulty) {
}
