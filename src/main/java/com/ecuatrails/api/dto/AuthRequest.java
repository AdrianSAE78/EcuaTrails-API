package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "AuthRequest", description = "Credenciales para autenticación local")
public class AuthRequest {
	@Schema(description = "Nombre de usuario", example = "ana")
	@NotBlank
	private String username;

	@Schema(description = "Contraseña", example = "Secr3t0!")
	@NotBlank
	private String password;

	public AuthRequest() {
	}

	public AuthRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}