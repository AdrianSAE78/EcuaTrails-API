package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RouteMap", description = "Trazado y puntos asociados para render de mapa")
public record RouteMap(@Schema(description = "ID de la ruta", example = "205") Integer routeId,
		@ArraySchema(arraySchema = @Schema(description = "Secuencia de puntos [lat, lng]"), minItems = 0, schema = @Schema(implementation = double[].class, description = "Par [lat, lng]", example = "[-2.9001, -79.0203]")) List<double[]> points,
		@ArraySchema(arraySchema = @Schema(description = "Ubicaciones de alojamientos [lat, lng]"), minItems = 0, schema = @Schema(implementation = double[].class, description = "Par [lat, lng]", example = "[-2.8999, -79.0220]")) List<double[]> lodgings) {
}
