package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AddFavoriteRequest", description = "Solicitud para marcar una ruta como favorita")
public record AddFavoriteRequest(
		@Schema(description = "ID de la ruta a marcar", example = "205") @NotNull Integer routeId) {
}
