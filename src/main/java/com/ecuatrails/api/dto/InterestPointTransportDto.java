package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "InterestPointTransportDto", description = "Transporte cercano a un punto de interés")
public record InterestPointTransportDto(@Schema(example = "9101") Integer relationId,
		@Schema(example = "301") Integer interestPointId,
		@Schema(example = "Mirador del Cóndor") String interestPointName, @Schema(example = "15") Integer transportId,
		@Schema(example = "Parada de bus - Vía Principal") String transportName,
		@Schema(description = "Distancia a pie (m)", example = "450") Integer walkingDistanceMeters,
		@Schema(description = "Tiempo a pie estimado (s)", example = "360") Integer estimatedWalkingTime,
		@Schema(description = "Notas de accesibilidad", example = "Camino empedrado, pendiente leve") String accessibilityNotes) {
}
