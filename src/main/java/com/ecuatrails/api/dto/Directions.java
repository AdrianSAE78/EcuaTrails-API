package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Directions", description = "Resultado de direcciones y ETA")
public record Directions(@Schema(description = "Latitud de origen", example = "-2.9001") double originLat,
		@Schema(description = "Longitud de origen", example = "-79.0203") double originLng,
		@Schema(description = "Latitud de destino", example = "-2.9012") double destLat,
		@Schema(description = "Longitud de destino", example = "-79.0187") double destLng,
		@Schema(description = "Modo de viaje", example = "walking", allowableValues = {
				"walking", "driving", "transit" }) String mode,
		@Schema(description = "Distancia aproximada en metros", example = "210") double distanceMeters,
		@Schema(description = "ETA aproximado en segundos", example = "168") int etaSeconds,
		@ArraySchema(arraySchema = @Schema(description = "Línea desde origen a destino (pares [lng, lat])"), schema = @Schema(implementation = double[].class, example = "[-79.0203, -2.9001]")) List<double[]> line){
}
