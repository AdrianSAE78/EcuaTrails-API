package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminLodgingListDto", description = "Item paginado de alojamiento (admin)")
public record AdminLodgingListDto(@Schema(example = "10") Integer id, @Schema(example = "Hostal Andino") String name,
		@Schema(example = "Cerca del centro") String description,
		@Schema(description = "Precio aprox. en USD", example = "35.0") BigDecimal approximatePrice,
		@Schema(description = "Latitud", example = "-2.9000") Float latitude,
		@Schema(description = "Longitud", example = "-79.0200") Float longitude,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
