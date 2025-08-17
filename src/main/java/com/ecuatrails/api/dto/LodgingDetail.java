package com.ecuatrails.api.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LodgingDetail", description = "Detalle de alojamiento")
public record LodgingDetail(@Schema(example = "10") Integer id, @Schema(example = "Hostería Andina") String name,
		@Schema(example = "Habitaciones con vista a la montaña") String description,
		@Schema(description = "Precio aproximado por noche (USD)", example = "45.00") BigDecimal approximatePrice,
		@Schema(description = "Latitud", example = "-2.90") Float latitude,
		@Schema(description = "Longitud", example = "-79.02") Float longitude,
		@Schema(description = "Imágenes del alojamiento") List<ImageDto> images,
		@Schema(description = "URL de imagen de portada", example = "https://cdn/acme/andina/cover.jpg") String coverImage) {
}