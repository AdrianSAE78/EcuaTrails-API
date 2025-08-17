package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RouteTransport", description = "Transporte agregado por ruta")
public record RouteTransport(@Schema(description = "ID de la ruta", example = "205") Integer routeId,
		@Schema(description = "Transportes asociados a PDIs de la ruta") List<InterestPointTransportDto> items) {
}