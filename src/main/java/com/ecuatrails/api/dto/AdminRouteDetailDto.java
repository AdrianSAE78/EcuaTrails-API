package com.ecuatrails.api.dto;

import java.time.Duration;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminRouteDetailDto", description = "Detalle administrativo de ruta")
public record AdminRouteDetailDto(@Schema(example = "102") Integer id, @Schema(example = "Ruta del Río") String name,
		@Schema(example = "Tramo junto al río") String description,
		@Schema(description = "ID de categoría", example = "1") Integer categoryId,
		@Schema(description = "Duración ISO-8601, p.ej. PT2H30M", example = "PT2H30M") Duration estimatedDuration,
		@Schema(description = "Horario recomendado", example = "08:00-12:00") String recommendedSchedule,
		@Schema(description = "Distancia (km)", example = "8.0") Float distance,
		@Schema(description = "Dificultad", example = "EASY") String difficulty,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}