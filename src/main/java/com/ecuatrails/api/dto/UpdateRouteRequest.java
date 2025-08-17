package com.ecuatrails.api.dto;

import java.time.Duration;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateRouteRequest", description = "Solicitud para actualizar ruta (parcial)")
public record UpdateRouteRequest(@Schema(example = "Ruta del Río (norte)") String name,
		@Schema(example = "Tramo junto al río, sector norte") String description,
		@Schema(description = "ID de categoría", example = "1") Integer categoryId,
		@Schema(description = "Duración ISO-8601", example = "PT3H") Duration estimatedDuration,
		@Schema(description = "Horario recomendado", example = "07:00-11:00") String recommendedSchedule,
		@Schema(description = "Distancia (km)", example = "9.2") Float distance,
		@Schema(description = "Dificultad", example = "MEDIUM") String difficulty,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
