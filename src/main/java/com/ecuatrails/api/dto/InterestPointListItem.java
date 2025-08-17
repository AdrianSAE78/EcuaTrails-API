package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "InterestPointListItem", description = "Item resumido de punto de interés")
public record InterestPointListItem(@Schema(example = "301") Integer id,
		@Schema(example = "Mirador del Cóndor") String name,
		@Schema(example = "Vista panorámica del valle") String description,
		@Schema(description = "Latitud", example = "-2.9001") Float latitude,
		@Schema(description = "Longitud", example = "-79.0203") Float longitude) {
}
