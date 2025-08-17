package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageRouteListResponse", description = "Página de rutas")
public record PageRouteListResponse(@Schema(description = "Elementos de la página") List<RouteListItem> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "5") int totalPages,
		@Schema(example = "41") long totalElements) {
}
