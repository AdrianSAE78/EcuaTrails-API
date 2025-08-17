package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminInterestPointList", description = "Item paginado de POI (admin)")
public record AdminInterestPointList(@Schema(example = "301") Integer id,
		@Schema(example = "Mirador del Cóndor") String name, @Schema(example = "Cuenca") String city,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status,
		@Schema(description = "Latitud", example = "-2.9001") Float latitude,
		@Schema(description = "Longitud", example = "-79.0203") Float longitude) {
}
