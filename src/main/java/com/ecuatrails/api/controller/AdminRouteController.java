package com.ecuatrails.api.controller;

import java.util.List;

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

import com.ecuatrails.api.dto.AdminRouteDetailDto;
import com.ecuatrails.api.dto.AdminRouteListDto;
import com.ecuatrails.api.dto.AdminRouteLodgingLinkDto;
import com.ecuatrails.api.dto.AdminRoutePoiLinkDto;
import com.ecuatrails.api.dto.CreateRouteLodgingLinkRequest;
import com.ecuatrails.api.dto.CreateRoutePoiLinkRequest;
import com.ecuatrails.api.dto.CreateRouteRequest;
import com.ecuatrails.api.dto.PageAdminRouteListResponse;
import com.ecuatrails.api.dto.ReorderRoutePoiRequest;
import com.ecuatrails.api.dto.UpdateRouteRequest;
import com.ecuatrails.api.service.AdminRouteService;

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
@RequestMapping("/api/admin/routes")
@Tag(name = "Admin • Routes", description = "Administración de rutas y sus relaciones")
public class AdminRouteController {

	private final AdminRouteService service;

	public AdminRouteController(AdminRouteService service) {
		this.service = service;
	}

// --- Routes ---

	@Operation(summary = "Listar rutas", description = "Lista paginada con filtros por texto, categoría y estado.", operationId = "adminListRoutes")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageAdminRouteListResponse.class), examples = @ExampleObject(name = "page", value = "{\n"
					+ "  \"content\": [\n"
					+ "    {\"id\": 101, \"name\": \"Ruta del Mirador\", \"categoryName\": \"Senderismo\", \"difficulty\": \"MEDIUM\", \"distance\": 12.5, \"status\": true}\n"
					+ "  ],\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"totalPages\": 5,\n"
					+ "  \"totalElements\": 42\n" + "}"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping
	public ResponseEntity<Page<AdminRouteListDto>> list(
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Texto de búsqueda", example = "mirador") @RequestParam(required = false) String q,
			@Parameter(description = "ID de categoría", example = "1") @RequestParam(required = false) Integer categoryId,
			@Parameter(description = "Estado (true=activo, false=inactivo)", example = "true") @RequestParam(required = false) Boolean status) {
		return ResponseEntity.ok(service.list(q, categoryId, status, page, size));
	}

	@Operation(summary = "Crear ruta", description = "Crea una nueva ruta.", operationId = "adminCreateRoute")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Creada", content = @Content(schema = @Schema(implementation = AdminRouteDetailDto.class), examples = @ExampleObject(value = "{ \"id\": 102, \"name\": \"Ruta del Río\", \"description\": \"Tramo junto al río\", \"categoryId\": 1, \"estimatedDuration\": \"PT2H30M\", \"recommendedSchedule\": \"08:00-12:00\", \"distance\": 8.0, \"difficulty\": \"EASY\", \"status\": true }"))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping
	public ResponseEntity<AdminRouteDetailDto> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Nueva ruta", content = @Content(schema = @Schema(implementation = CreateRouteRequest.class))) @Valid @RequestBody CreateRouteRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	@Operation(summary = "Actualizar ruta", description = "Actualiza parcialmente una ruta por ID.", operationId = "adminUpdateRoute")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizada", content = @Content(schema = @Schema(implementation = AdminRouteDetailDto.class))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta o categoría no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}")
	public ResponseEntity<AdminRouteDetailDto> update(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Campos a actualizar (parciales)", content = @Content(schema = @Schema(implementation = UpdateRouteRequest.class))) @Valid @RequestBody UpdateRouteRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	@Operation(summary = "Eliminar ruta", description = "Elimina la ruta si no tiene relaciones (POIs, alojamientos).", operationId = "adminDeleteRoute")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminada"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Tiene relaciones activas", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

// --- POIs ---

	@Operation(summary = "Listar POIs de una ruta", description = "Devuelve los enlaces (ordenados por posición).", operationId = "adminListRoutePois")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AdminRoutePoiLinkDto.class), examples = @ExampleObject(name = "lista", value = "[{\"routeInterestPointId\": 501, \"interestPointId\": 301, \"interestPointName\": \"Mirador\", \"position\": 0}]"))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/{id}/interest-points")
	public ResponseEntity<List<AdminRoutePoiLinkDto>> listPois(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id) {
		return ResponseEntity.ok(service.listPois(id));
	}

	@Operation(summary = "Agregar POI a una ruta", description = "Crea el enlace Route-POI. Si no se envía posición, se usa 0 o el orden por defecto.", operationId = "adminAddRoutePoi")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Agregado", content = @Content(schema = @Schema(implementation = AdminRoutePoiLinkDto.class), examples = @ExampleObject(value = "{ \"routeInterestPointId\": 502, \"interestPointId\": 302, \"interestPointName\": \"Cascada\", \"position\": 1 }"))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta o POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/{id}/interest-points")
	public ResponseEntity<AdminRoutePoiLinkDto> addPoi(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "POI a enlazar", content = @Content(schema = @Schema(implementation = CreateRoutePoiLinkRequest.class))) @Valid @RequestBody CreateRoutePoiLinkRequest body) {
		return ResponseEntity.ok(service.addPoi(id, body));
	}

	@Operation(summary = "Eliminar enlace Route-POI", description = "Elimina el vínculo específico.", operationId = "adminDeleteRoutePoi")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado"),
			@ApiResponse(responseCode = "404", description = "Vínculo o ruta no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}/interest-points/{routeInterestPointId}")
	public ResponseEntity<Void> deletePoi(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id,
			@Parameter(description = "ID del vínculo", example = "501") @PathVariable Integer routeInterestPointId) {
		service.deletePoi(id, routeInterestPointId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Reordenar POIs de una ruta", description = "Actualiza la posición según `orderedIds`.", operationId = "adminReorderRoutePois")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AdminRoutePoiLinkDto.class), examples = @ExampleObject(name = "lista", value = "[{\"routeInterestPointId\": 501, \"interestPointId\": 301, \"interestPointName\": \"Mirador\", \"position\": 1}]"))),
			@ApiResponse(responseCode = "404", description = "Ruta o vínculo no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}/interest-points/reorder")
	public ResponseEntity<List<AdminRoutePoiLinkDto>> reorderPois(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "IDs en el nuevo orden", content = @Content(schema = @Schema(implementation = ReorderRoutePoiRequest.class), examples = @ExampleObject(value = "{ \"orderedIds\": [502, 501] }"))) @Valid @RequestBody ReorderRoutePoiRequest body) {
		return ResponseEntity.ok(service.reorderPois(id, body));
	}

// --- Lodgings ---

	@Operation(summary = "Listar alojamientos de una ruta", description = "Devuelve los enlaces Route-Lodging.", operationId = "adminListRouteLodgings")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AdminRouteLodgingLinkDto.class), examples = @ExampleObject(name = "lista", value = "[{\"routeLodgingId\": 601, \"lodgingId\": 11, \"lodgingName\": \"Hostal Andino\"}]"))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/{id}/lodgings")
	public ResponseEntity<List<AdminRouteLodgingLinkDto>> listLodgings(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id) {
		return ResponseEntity.ok(service.listLodgings(id));
	}

	@Operation(summary = "Agregar alojamiento a una ruta", description = "Crea el enlace Route-Lodging.", operationId = "adminAddRouteLodging")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Agregado", content = @Content(schema = @Schema(implementation = AdminRouteLodgingLinkDto.class), examples = @ExampleObject(value = "{ \"routeLodgingId\": 602, \"lodgingId\": 12, \"lodgingName\": \"Hotel Mirador\" }"))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta o alojamiento no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/{id}/lodgings")
	public ResponseEntity<AdminRouteLodgingLinkDto> addLodging(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Alojamiento a enlazar", content = @Content(schema = @Schema(implementation = CreateRouteLodgingLinkRequest.class))) @Valid @RequestBody CreateRouteLodgingLinkRequest body) {
		return ResponseEntity.ok(service.addLodging(id, body));
	}

	@Operation(summary = "Eliminar enlace Route-Lodging", description = "Elimina el vínculo específico.", operationId = "adminDeleteRouteLodging")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado"),
			@ApiResponse(responseCode = "404", description = "Vínculo o ruta no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}/lodgings/{routeLodgingId}")
	public ResponseEntity<Void> deleteLodging(
			@Parameter(description = "ID de la ruta", example = "102") @PathVariable Integer id,
			@Parameter(description = "ID del vínculo", example = "601") @PathVariable Integer routeLodgingId) {
		service.deleteLodging(id, routeLodgingId);
		return ResponseEntity.noContent().build();
	}
}
