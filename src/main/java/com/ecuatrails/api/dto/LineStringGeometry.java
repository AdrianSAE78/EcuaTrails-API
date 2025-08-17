package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LineStringGeometry", description = "Geometría LineString GeoJSON")
public record LineStringGeometry(@Schema(example = "LineString") String type,
		@ArraySchema(arraySchema = @Schema(description = "Secuencia de posiciones [lng, lat]"), schema = @Schema(implementation = double[].class, example = "[-79.0203, -2.9001]")) List<double[]> coordinates) {
}
