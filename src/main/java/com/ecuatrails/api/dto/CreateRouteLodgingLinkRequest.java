package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateRouteLodgingLinkRequest", description = "Solicitud para enlazar alojamiento")
public record CreateRouteLodgingLinkRequest(
		@Schema(description = "ID del alojamiento", example = "11") Integer lodgingId) {
}
