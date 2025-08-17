package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GeoJsonFeatureCollection", description = "Colección GeoJSON de features")
public record GeoJsonFeatureCollection(
    @Schema(example = "FeatureCollection") String type,
    @Schema(description = "Listado de features") List<GeoJsonFeature> features
) {}
