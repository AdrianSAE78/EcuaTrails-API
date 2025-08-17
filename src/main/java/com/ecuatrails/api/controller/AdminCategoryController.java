package com.ecuatrails.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.CategoryDto;
import com.ecuatrails.api.dto.CreateCategoryRequest;
import com.ecuatrails.api.dto.UpdateCategoryRequest;
import com.ecuatrails.api.service.AdminCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/categories")
@Tag(name = "Admin • Categories", description = "Gestión de categorías (solo ADMIN)")
public class AdminCategoryController {

	private final AdminCategoryService service;

	public AdminCategoryController(AdminCategoryService service) {
		this.service = service;
	}

	@Operation(summary = "Listar categorías", description = "Devuelve todas las categorías ordenadas por nombre.", operationId = "adminListCategories")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CategoryDto.class), examples = @ExampleObject(value = """
					[
					  {"id":1,"code":"HIKING","name":"Senderismo"},
					  {"id":2,"code":"BIKE","name":"Ciclismo"}
					]
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (requiere rol ADMIN)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping
	public ResponseEntity<List<CategoryDto>> list() {
		return ResponseEntity.ok(service.list());
	}

	@Operation(summary = "Crear categoría", description = """
			Crea una nueva categoría. Reglas:
			- `code` único (case-insensitive), máx 16 chars, requerido.
			- `name` único (case-insensitive), máx 64 chars, requerido.
			""", operationId = "adminCreateCategory")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Creada", content = @Content(schema = @Schema(implementation = CategoryDto.class), examples = @ExampleObject(value = """
					{ "id": 3, "code": "CLIMB", "name": "Escalada" }
					"""))),
			@ApiResponse(responseCode = "400", description = "Validación fallida (code/name requeridos o longitud)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (requiere rol ADMIN)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Duplicado (code o name ya existen)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping
	public ResponseEntity<CategoryDto> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Nueva categoría", content = @Content(schema = @Schema(implementation = CreateCategoryRequest.class), examples = @ExampleObject(value = """
					{ "code": "CLIMB", "name": "Escalada" }
					"""))) @Valid @RequestBody CreateCategoryRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	@Operation(summary = "Actualizar categoría", description = """
			Actualiza parcialmente `code` y/o `name`. Reglas:
			- Si se envía `code`, no vacío, máx 16 chars, y único.
			- Si se envía `name`, no vacío, máx 64 chars, y único.
			""", operationId = "adminUpdateCategory")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizada", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
			@ApiResponse(responseCode = "400", description = "Validación fallida (cadenas vacías o longitudes)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (requiere rol ADMIN)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Duplicado (code o name ya existen)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> update(
			@Parameter(description = "ID de la categoría", example = "3") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Campos a actualizar (parciales)", content = @Content(schema = @Schema(implementation = UpdateCategoryRequest.class), examples = @ExampleObject(value = """
					{ "name": "Escalada en roca" }
					"""))) @Valid @RequestBody UpdateCategoryRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	@Operation(summary = "Eliminar categoría", description = "Elimina la categoría si **no** está en uso por rutas.", operationId = "adminDeleteCategory")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminada"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (requiere rol ADMIN)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "En uso por rutas", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID de la categoría", example = "3") @PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
