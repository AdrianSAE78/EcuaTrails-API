package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageAdminInterestPointListResponse", description = "Página de POIs (admin)")
public record PageAdminInterestPointListResponse(
		@Schema(description = "Elementos de la página") List<AdminInterestPointList> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "3") int totalPages,
		@Schema(example = "24") long totalElements) {
}
