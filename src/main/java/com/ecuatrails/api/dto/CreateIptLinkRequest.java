package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateIptLinkRequest", description = "Solicitud para enlazar transporte a POI")
public record CreateIptLinkRequest(@Schema(description = "ID del transporte", example = "1") Integer transportId,
		@Schema(description = "Distancia a pie (m)", example = "300") Integer walkingDistanceMeters,
		@Schema(description = "Tiempo a pie estimado (min)", example = "4") Integer estimatedWalkingTime,
		@Schema(description = "Notas de accesibilidad", example = "Cruce peatonal") String accessibilityNotes,
		@Schema(description = "Estado del vínculo", example = "true") Boolean status) {
}
