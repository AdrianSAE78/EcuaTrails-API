package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UploadRouteImageForm", description = "Formulario para subir imagen de ruta", requiredProperties = {
		"file" })
public class UploadRouteImageForm {
	@Schema(description = "Imagen a subir", type = "string", format = "binary")
	public org.springframework.web.multipart.MultipartFile file;

	@Schema(description = "Título opcional", example = "Portada")
	public String title;

	@Schema(description = "Texto alternativo", example = "Mirador principal")
	public String alt;

	@Schema(description = "Marcar como portada", example = "false")
	public Boolean cover;
}