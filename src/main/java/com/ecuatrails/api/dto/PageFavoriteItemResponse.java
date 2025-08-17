package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageFavoriteItemResponse", description = "Página de favoritos")
public record PageFavoriteItemResponse(
    @Schema(description = "Elementos de la página")
    List<FavoriteItemDto> content,
    @Schema(example = "0") int page,
    @Schema(example = "10") int size,
    @Schema(example = "3") int totalPages,
    @Schema(example = "21") long totalElements
) {}
