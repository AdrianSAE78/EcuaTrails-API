package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "PageLodgingListResponse", description = "Página de alojamientos")
public record PageLodgingListResponse(@Schema(description = "Elementos de la página") List<LodgingListItem> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "5") int totalPages,
		@Schema(example = "41") long totalElements) {
}
