package com.ecuatrails.api.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.AdminLodgingDetailDto;
import com.ecuatrails.api.dto.AdminLodgingListDto;
import com.ecuatrails.api.dto.CreateLodgingRequest;
import com.ecuatrails.api.dto.PageAdminLodgingListResponse;
import com.ecuatrails.api.dto.UpdateLodgingRequest;
import com.ecuatrails.api.service.AdminLodgingService;

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
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/lodgings")
@Tag(name = "Admin • Lodgings", description = "Administración de alojamientos (hoteles/hostales)")
public class AdminLodgingController {

	private final AdminLodgingService service;

	public AdminLodgingController(AdminLodgingService service) {
		this.service = service;
	}

	@Operation(summary = "Listar alojamientos", description = "Devuelve una lista paginada con filtros por texto, estado y rango de precio.", operationId = "adminListLodgings")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageAdminLodgingListResponse.class), examples = @ExampleObject(name = "page", value = "{\n"
					+ "  \"content\": [\n"
					+ "    {\"id\": 10, \"name\": \"Hostal Andino\", \"description\": \"Cerca del centro\", \"approximatePrice\": 35.0, \"latitude\": -2.90, \"longitude\": -79.02, \"status\": true}\n"
					+ "  ],\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"totalPages\": 3,\n"
					+ "  \"totalElements\": 24\n" + "}"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping
	public ResponseEntity<Page<AdminLodgingListDto>> list(
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Texto de búsqueda", example = "andino") @RequestParam(required = false) String q,
			@Parameter(description = "Estado: true=activo, false=inactivo", example = "true") @RequestParam(required = false) Boolean status,
			@Parameter(description = "Precio mínimo", example = "20") @RequestParam(required = false) BigDecimal minPrice,
			@Parameter(description = "Precio máximo", example = "100") @RequestParam(required = false) BigDecimal maxPrice) {
		return ResponseEntity.ok(service.list(q, status, minPrice, maxPrice, page, size));
	}

	@Operation(summary = "Crear alojamiento", description = "Crea un nuevo alojamiento.", operationId = "adminCreateLodging")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Creado", content = @Content(schema = @Schema(implementation = AdminLodgingDetailDto.class), examples = @ExampleObject(value = "{ \"id\": 11, \"name\": \"Hotel Mirador\", \"description\": \"Vista a la ciudad\", \"approximatePrice\": 50.0, \"latitude\": -2.901, \"longitude\": -79.019, \"status\": true }"))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping
	public ResponseEntity<AdminLodgingDetailDto> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Nuevo alojamiento", content = @Content(schema = @Schema(implementation = CreateLodgingRequest.class))) @Valid @RequestBody CreateLodgingRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	@Operation(summary = "Actualizar alojamiento", description = "Actualiza parcialmente un alojamiento por ID.", operationId = "adminUpdateLodging")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizado", content = @Content(schema = @Schema(implementation = AdminLodgingDetailDto.class))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Alojamiento no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}")
	public ResponseEntity<AdminLodgingDetailDto> update(
			@Parameter(description = "ID del alojamiento", example = "11") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Campos a actualizar (parciales)", content = @Content(schema = @Schema(implementation = UpdateLodgingRequest.class))) @Valid @RequestBody UpdateLodgingRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	@Operation(summary = "Eliminar alojamiento", description = "Elimina el alojamiento si no está en uso por rutas.", operationId = "adminDeleteLodging")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Alojamiento no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "En uso por rutas", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID del alojamiento", example = "11") @PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}