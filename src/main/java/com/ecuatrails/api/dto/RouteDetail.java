package com.ecuatrails.api.dto;

import java.time.Duration;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RouteDetail", description = "Detalle de ruta con PDI y alojamientos")
public record RouteDetail(@Schema(example = "205") Integer id, @Schema(example = "Mirador del Cóndor") String name,
		@Schema(example = "Ascenso con vistas panorámicas") String description,
		@Schema(description = "Nombre de categoría legible", example = "Senderismo") String category,
		@Schema(description = "Duración estimada (ISO-8601)", example = "PT4H") Duration estimatedDuration,
		@Schema(description = "Distancia total (km)", example = "12.1") Float distance,
		@Schema(description = "Dificultad (EASY, MEDIUM, HARD)", example = "MEDIUM") String difficulty,
		@Schema(description = "Horario recomendado", example = "08:00-12:00") String recommendedSchedule,
		@Schema(description = "Puntos de interés asociados") List<Poi> interestPoints,
		@Schema(description = "Alojamientos cercanos") List<LodgingDto> lodgings,
		@Schema(description = "Imágenes de la ruta") List<ImageDto> images,
		@Schema(description = "URL de portada", example = "https://cdn/acme/routes/205/cover.jpg") String coverImage) {
}
