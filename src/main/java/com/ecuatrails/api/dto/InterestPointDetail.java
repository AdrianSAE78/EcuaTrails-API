package com.ecuatrails.api.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "InterestPointDetail", description = "Detalle de punto de interés")
public record InterestPointDetail(
    @Schema(example = "301") Integer id,
    @Schema(example = "Mirador del Cóndor") String name,
    @Schema(example = "Vista panorámica del valle") String description,
    @Schema(description = "Latitud", example = "-2.9001") Float latitude,
    @Schema(description = "Longitud", example = "-79.0203") Float longitude,
    @Schema(description = "Dirección o referencia", example = "Km 5 vía al mirador") String address,
    @Schema(description = "Ciudad", example = "Cuenca") String city,
    @Schema(description = "Horario de atención (libre o ISO-8601)", example = "Lun-Dom 08:00-18:00") String openingHours,
    @Schema(description = "Calificación promedio (0–5)", example = "4.6") BigDecimal rating,
    @Schema(description = "Cantidad de reseñas", example = "128") Integer reviewCount,
    @Schema(description = "Imágenes del punto de interés") List<ImageDto> images,
    @Schema(description = "URL de portada", example = "https://cdn/acme/poi/301/cover.jpg") String coverImage
) {}
