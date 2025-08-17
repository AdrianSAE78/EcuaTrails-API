package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StatusRequest", description = "Cambio de estado del POI")
public record StatusRequest(@Schema(description = "true=activo, false=inactivo", example = "true") Boolean status) {
}
