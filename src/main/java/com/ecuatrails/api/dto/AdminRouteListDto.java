package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminRouteListDto", description = "Item de ruta en listado admin")
public record AdminRouteListDto(@Schema(example = "101") Integer id, @Schema(example = "Ruta del Mirador") String name,
		@Schema(example = "Senderismo") String categoryName, @Schema(example = "MEDIUM") String difficulty,
		@Schema(description = "Distancia (km)", example = "12.5") Float distance,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
