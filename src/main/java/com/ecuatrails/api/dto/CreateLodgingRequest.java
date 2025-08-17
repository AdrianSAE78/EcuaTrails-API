package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateLodgingRequest", description = "Solicitud para crear alojamiento")
public record CreateLodgingRequest(@Schema(example = "Hotel Mirador") String name,
		@Schema(example = "Vista a la ciudad") String description,
		@Schema(description = "Precio aprox. en USD", example = "50.0") BigDecimal approximatePrice,
		@Schema(description = "Latitud", example = "-2.9010") Float latitude,
		@Schema(description = "Longitud", example = "-79.0190") Float longitude,
		@Schema(description = "Estado inicial", example = "true") Boolean status) {
}