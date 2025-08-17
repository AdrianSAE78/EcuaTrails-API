package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateInterestPointRequest", description = "Request para crear POI")
public record CreateInterestPointRequest(@Schema(example = "Mirador del Cóndor") String name,
		@Schema(example = "Vista panorámica del valle") String description,
		@Schema(example = "Km 5 vía al mirador") String address, @Schema(example = "Cuenca") String city,
		@Schema(example = "Lun-Dom 08:00-18:00") String openingHours,
		@Schema(example = "4.6") java.math.BigDecimal rating, @Schema(example = "128") Integer reviewCount,
		@Schema(description = "Latitud", example = "-2.9001") Float latitude,
		@Schema(description = "Longitud", example = "-79.0203") Float longitude,
		@Schema(description = "Estado inicial", example = "true") Boolean status) {
}