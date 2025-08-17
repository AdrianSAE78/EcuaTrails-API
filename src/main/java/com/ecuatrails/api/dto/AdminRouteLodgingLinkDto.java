package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminRouteLodgingLinkDto", description = "Vínculo Route-Lodging")
public record AdminRouteLodgingLinkDto(@Schema(example = "601") Integer routeLodgingId,
		@Schema(example = "11") Integer lodgingId, @Schema(example = "Hostal Andino") String lodgingName) {
}