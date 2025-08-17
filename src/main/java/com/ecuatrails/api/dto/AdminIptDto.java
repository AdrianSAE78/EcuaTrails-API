package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminIptDto", description = "Vínculo transporte-POI")
public record AdminIptDto(@Schema(description = "ID del vínculo", example = "101") Integer id,
		@Schema(description = "ID del POI", example = "301") Integer interestPointId,
		@Schema(description = "Nombre del POI", example = "Mirador") String interestPointName,
		@Schema(description = "ID del transporte", example = "2") Integer transportId,
		@Schema(description = "Nombre del transporte", example = "Metro A") String transportName,
		@Schema(description = "Distancia a pie (m)", example = "350") Integer walkingDistanceMeters,
		@Schema(description = "Tiempo a pie estimado (min)", example = "5") Integer estimatedWalkingTime,
		@Schema(description = "Notas de accesibilidad", example = "Rampa disponible") String accessibilityNotes,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}