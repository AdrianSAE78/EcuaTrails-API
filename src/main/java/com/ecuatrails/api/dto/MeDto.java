package com.ecuatrails.api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MeDto", description = "Perfil del usuario autenticado")
public record MeDto(@Schema(example = "42") Integer id, @Schema(example = "Ana") String name,
		@Schema(example = "Pérez") String lastName, @Schema(example = "ana") String username,
		@Schema(example = "ana@acme.com") String email,
		@Schema(description = "Fecha de nacimiento (YYYY-MM-DD)", example = "1995-03-15") LocalDate birthday,
		@Schema(description = "Proveedor de autenticación", example = "LOCAL") String authProvider,
		@Schema(description = "UID externo (por ej. Firebase)", example = "e4b8a1...") String uid) {
}
