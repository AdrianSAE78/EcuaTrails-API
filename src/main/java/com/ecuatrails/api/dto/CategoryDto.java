package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategoryDto", description = "Categoría de rutas")
public record CategoryDto(@Schema(example = "1") Integer id, @Schema(example = "HIKING") String code,
		@Schema(example = "Senderismo") String name) {
}
