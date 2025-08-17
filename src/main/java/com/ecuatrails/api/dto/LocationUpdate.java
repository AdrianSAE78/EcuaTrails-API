package com.ecuatrails.api.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "LocationUpdate", description = "Payload de actualización de ubicación")
public record LocationUpdate(@Schema(description = "ID de la ruta", example = "205") @NotNull Integer routeId,
		@Schema(description = "Latitud", example = "-2.898486") @NotNull @DecimalMin(value = "-90") @DecimalMax(value = "90") Double lat,
		@Schema(description = "Longitud", example = "-79.026149") @NotNull @DecimalMin(value = "-180") @DecimalMax(value = "180") Double lng,
		@Schema(description = "Velocidad en m/s", example = "1.2") @PositiveOrZero Double speed,
		@Schema(description = "Rumbo 0–360 (0=N)", example = "170") @Min(0) @Max(360) Integer heading,
		@Schema(description = "Marca de tiempo ISO-8601", example = "2025-08-16T19:12:33Z") @NotNull Instant timestamp) {
}