package com.ecuatrails.api.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateHistoryRequest", description = "Solicitud para crear un registro de historial")
public record CreateHistoryRequest(@Schema(description = "ID de la ruta", example = "205") @NotNull Integer routeId,
		@Schema(description = "Indica si la ruta fue finalizada", example = "false") Boolean isFinished,
		@Schema(description = "Fecha/hora de la ruta (ISO-8601). Si no se envía, se usa la hora actual.", example = "2025-08-16T09:30:00") LocalDateTime routeDate) {
}