package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateCategoryRequest", description = "Solicitud para crear categoría")
public record CreateCategoryRequest(
		@Schema(description = "Código único (≤16)", example = "HIKING") @NotBlank @Size(max = 16) String code,
		@Schema(description = "Nombre visible (≤64)", example = "Senderismo") @NotBlank @Size(max = 64) String name) {
}
