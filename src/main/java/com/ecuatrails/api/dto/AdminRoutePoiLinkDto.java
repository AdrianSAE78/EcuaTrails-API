package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminRoutePoiLinkDto", description = "Vínculo Route-POI")
public record AdminRoutePoiLinkDto(@Schema(example = "501") Integer routeInterestPointId,
		@Schema(example = "301") Integer interestPointId,
		@Schema(example = "Mirador del Cóndor") String interestPointName,
		@Schema(description = "Posición (0..n)", example = "0") Integer position) {
}
