package com.ecuatrails.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminInterestPointDetail", description = "Detalle administrativo de POI")
public record AdminInterestPointDetail(@Schema(example = "301") Integer id,
		@Schema(example = "Mirador del Cóndor") String name,
		@Schema(example = "Vista panorámica del valle") String description,
		@Schema(example = "Km 5 vía al mirador") String address, @Schema(example = "Cuenca") String city,
		@Schema(description = "Horario de atención", example = "Lun-Dom 08:00-18:00") String openingHours,
		@Schema(description = "Calificación promedio (0–5)", example = "4.6") BigDecimal rating,
		@Schema(description = "Cantidad de reseñas", example = "128") Integer reviewCount,
		@Schema(description = "Latitud", example = "-2.9001") Float latitude,
		@Schema(description = "Longitud", example = "-79.0203") Float longitude,
		@Schema(description = "Estado activo/inactivo", example = "true") Boolean status) {
}
