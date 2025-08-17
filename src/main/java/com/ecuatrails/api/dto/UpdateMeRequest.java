package com.ecuatrails.api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(name = "UpdateMeRequest", description = "Campos editables del perfil (parciales)")
public record UpdateMeRequest(@Schema(example = "Ana María") String name, @Schema(example = "Pérez") String lastName,
		@Schema(description = "Correo del usuario", example = "ana.maria@acme.com") @Email(message = "Email inválido") String email,
		@Schema(description = "Fecha de nacimiento (YYYY-MM-DD)", example = "1995-03-15") LocalDate birthday) {
}
