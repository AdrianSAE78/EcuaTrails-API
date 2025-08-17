package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiValidationError", description = "Error de validación de un campo")
public record ApiValidationError(@Schema(example = "name") String field,
		@Schema(example = "must not be blank") String message, @Schema(example = "   ") Object rejectedValue) {
}
