package com.ecuatrails.api.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GeoJsonFeature", description = "Feature GeoJSON")
public record GeoJsonFeature(
    @Schema(example = "Feature") String type,
    @Schema(implementation = LineStringGeometry.class) Object geometry,
    @Schema(description = "Atributos arbitrarios") Map<String, Object> properties
) {}