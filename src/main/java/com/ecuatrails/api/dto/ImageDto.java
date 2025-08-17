package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ImageDto", description = "Imagenes")
public record ImageDto(@Schema(example = "12") Integer id, @Schema(example = "https://cdn/poi/301/2.jpg") String url,
		@Schema(example = "Sendero") String title, @Schema(example = "Sendero hacia el mirador") String alt,
		@Schema(description = "Portada", example = "false") Boolean cover,
		@Schema(description = "Posición (0..n)", example = "1") Integer position) {
}