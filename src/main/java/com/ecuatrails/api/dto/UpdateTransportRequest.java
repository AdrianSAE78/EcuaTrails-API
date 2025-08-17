package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateTransportRequest", description = "Solicitud para actualizar transporte (parcial)")
public record UpdateTransportRequest(@Schema(example = "Bus 24 - Exprés") String name,
		@Schema(example = "BUS") String type,
		@Schema(description = "Ruta o corredor", example = "Av. Central - Exprés") String route,
		@Schema(description = "Línea (si aplica)", example = "24") String busLine,
		@Schema(description = "Horario", example = "06:00-21:00") String schedule,
		@Schema(description = "Tarifa base (USD)", example = "0.40") BigDecimal baseFare,
		@Schema(description = "Accesible", example = "true") Boolean accessibility,
		@Schema(description = "Activo/inactivo", example = "true") Boolean status) {
}
