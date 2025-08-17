package com.ecuatrails.api.dto;

import java.math.BigDecimal;
import java.time.Duration;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Preference", description = "Preferencias del usuario")
public record Preference(@Schema(description = "ID de la categoría preferida", example = "1") Integer categoryId,
		@Schema(description = "Presupuesto preferido en USD", example = "80.00") BigDecimal preferedBudget,
		@Schema(description = "Duración preferida (ISO-8601)", example = "PT3H") Duration preferedDuration) {
}
