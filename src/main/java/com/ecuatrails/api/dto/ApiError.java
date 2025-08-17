package com.ecuatrails.api.dto;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiError", description = "Estructura base de error")
public record ApiError(@Schema(example = "2025-08-16T14:23:11Z") Instant timestamp, @Schema(example = "404") int status,
		@Schema(example = "Not Found") String error, @Schema(example = "Route not found") String message,
		@Schema(example = "/api/routes/999") String path, @Schema(example = "req-8b2f1c") String traceId,
		@Schema(description = "Detalle de validación por campo") List<ApiValidationError> errors) {
}
