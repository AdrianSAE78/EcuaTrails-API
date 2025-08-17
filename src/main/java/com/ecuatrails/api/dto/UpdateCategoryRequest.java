package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateCategoryRequest", description = "Solicitud para actualizar categoría (parcial)")
public record UpdateCategoryRequest(
    @Schema(description = "Código único (≤16). Si se envía, no puede ser vacío.", example = "HIKING")
    @Size(max = 16) String code,
    @Schema(description = "Nombre visible (≤64). Si se envía, no puede ser vacío.", example = "Senderismo")
    @Size(max = 64) String name
) {}
