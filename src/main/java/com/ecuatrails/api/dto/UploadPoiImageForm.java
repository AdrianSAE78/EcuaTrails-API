package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UploadPoiImageForm", description = "Formulario para subir imagen", requiredProperties = { "file" })
public class UploadPoiImageForm {
	@Schema(description = "Imagen a subir", type = "string", format = "binary")
	public org.springframework.web.multipart.MultipartFile file;

	@Schema(description = "Título opcional", example = "Fachada")
	public String title;

	@Schema(description = "Texto alternativo", example = "Entrada principal")
	public String alt;

	@Schema(description = "Marcar como portada", example = "false")
	public Boolean cover;
}
