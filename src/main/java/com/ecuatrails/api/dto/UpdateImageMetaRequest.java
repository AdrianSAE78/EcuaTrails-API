package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateImageMetaRequest", description = "Metadatos editables de imagen")
public record UpdateImageMetaRequest(@Schema(example = "Panorámica") String title,
		@Schema(example = "Vista al valle") String alt,
		@Schema(description = "Marcar como portada", example = "true") Boolean cover) {
}
