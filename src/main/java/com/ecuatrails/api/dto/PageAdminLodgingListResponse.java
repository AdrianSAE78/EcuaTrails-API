package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageAdminLodgingListResponse", description = "Página de alojamientos (admin)")
public record PageAdminLodgingListResponse(
		@Schema(description = "Elementos de la página") List<AdminLodgingListDto> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "3") int totalPages,
		@Schema(example = "24") long totalElements) {
}
