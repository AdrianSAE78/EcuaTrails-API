package com.ecuatrails.api.dto;

import java.math.BigDecimal;
import java.time.Duration;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdatePreferenceRequest", description = "Campos editables de preferencias (parciales)")
public record UpdatePreferenceRequest(
		@Schema(description = "ID de categoría preferida", example = "1") Integer categoryId,
		@Schema(description = "Presupuesto preferido en USD", example = "100.00") BigDecimal preferedBudget,
		@Schema(description = "Duración preferida (ISO-8601)", example = "PT2H30M") Duration preferedDuration) {
}