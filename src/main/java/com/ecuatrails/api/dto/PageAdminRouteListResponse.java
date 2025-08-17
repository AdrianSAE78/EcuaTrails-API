package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageAdminRouteListResponse", description = "Página de rutas (admin)")
public record PageAdminRouteListResponse(
		@Schema(description = "Elementos de la página") List<AdminRouteListDto> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "5") int totalPages,
		@Schema(example = "42") long totalElements) {
}
