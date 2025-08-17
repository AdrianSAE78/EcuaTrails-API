package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminTransportDetail", description = "Detalle administrativo de transporte")
public record AdminTransportDetail(@Schema(example = "2") Integer id, @Schema(example = "Metro A") String name,
		@Schema(example = "METRO") String type,
		@Schema(description = "Ruta o corredor", example = "Troncal A") String route,
		@Schema(description = "Línea (si aplica)", example = "A") String busLine,
		@Schema(description = "Horario", example = "06:00-22:00") String schedule,
		@Schema(description = "Tarifa base (USD)", example = "0.45") BigDecimal baseFare,
		@Schema(description = "Accesible", example = "true") Boolean accessibility,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
