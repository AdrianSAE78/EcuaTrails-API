package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateTransportRequest", description = "Solicitud para crear transporte")
public record CreateTransportRequest(@Schema(example = "Bus 24") String name, @Schema(example = "BUS") String type,
		@Schema(description = "Ruta o corredor", example = "Av. Central") String route,
		@Schema(description = "Línea (si aplica)", example = "24") String busLine,
		@Schema(description = "Horario", example = "06:00-20:00") String schedule,
		@Schema(description = "Tarifa base (USD)", example = "0.35") BigDecimal baseFare,
		@Schema(description = "Accesible", example = "true") Boolean accessibility,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}