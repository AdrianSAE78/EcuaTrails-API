package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GeocodeResponse", description = "Resultado de geocodificación")
public record GeocodeResponse(@Schema(description = "Latitud", example = "-2.9001") Float latitude,
		@Schema(description = "Longitud", example = "-79.0203") Float longitude) {
}