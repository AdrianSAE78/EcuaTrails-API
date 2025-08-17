package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateRoutePoiLinkRequest", description = "Solicitud para enlazar POI")
public record CreateRoutePoiLinkRequest(@Schema(description = "ID del POI", example = "301") Integer interestPointId,
		@Schema(description = "Posición deseada (opcional)", example = "0") Integer position) {
}
