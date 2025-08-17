package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminTransportList", description = "Item paginado de transporte (admin)")
public record AdminTransportList(@Schema(example = "1") Integer id, @Schema(example = "Bus 24") String name,
		@Schema(example = "BUS") String type,
		@Schema(description = "Ruta o corredor", example = "Av. Central") String route,
		@Schema(description = "Línea (si aplica)", example = "24") String busLine,
		@Schema(description = "Tarifa base (USD)", example = "0.35") BigDecimal baseFare,
		@Schema(description = "Accesible", example = "true") Boolean accessibility,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
