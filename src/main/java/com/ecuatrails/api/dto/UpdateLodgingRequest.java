package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateLodgingRequest", description = "Solicitud para actualizar alojamiento (parcial)")
public record UpdateLodgingRequest(@Schema(example = "Hotel Mirador") String name,
		@Schema(example = "Vista a la ciudad") String description,
		@Schema(description = "Precio aprox. en USD", example = "55.0") BigDecimal approximatePrice,
		@Schema(description = "Latitud", example = "-2.9010") Float latitude,
		@Schema(description = "Longitud", example = "-79.0190") Float longitude,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
