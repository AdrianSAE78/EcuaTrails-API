package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UploadImageResponse", description = "Respuesta de subida")
public record UploadImageResponse(@Schema(description = "Imagen creada/actualizada") ImageDto image) {
}
