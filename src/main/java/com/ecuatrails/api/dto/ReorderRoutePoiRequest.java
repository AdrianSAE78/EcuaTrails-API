package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReorderRoutePoiRequest", description = "IDs de vínculos en el nuevo orden")
public record ReorderRoutePoiRequest(@Schema(example = "[502, 501]") List<Integer> orderedIds) {
}
