package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReorderImagesRequest", description = "Orden deseado por IDs")
public record ReorderImagesRequest(@Schema(example = "[12,11,13]") List<Integer> orderedIds) {
}