package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageHistoryItemResponse", description = "Página de historial")
public record PageHistoryItemResponse(@Schema(description = "Elementos de la página") List<HistoryItem> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "2") int totalPages,
		@Schema(example = "17") long totalElements) {
}