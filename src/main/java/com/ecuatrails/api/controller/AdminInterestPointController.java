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

import com.ecuatrails.api.dto.AdminInterestPointDetail;
import com.ecuatrails.api.dto.AdminInterestPointList;
import com.ecuatrails.api.dto.AdminPoiTransportLinkDto;
import com.ecuatrails.api.dto.BulkInterestPointRequest;
import com.ecuatrails.api.dto.CreateInterestPointRequest;
import com.ecuatrails.api.dto.CreateIptLinkRequest;
import com.ecuatrails.api.dto.GeocodeResponse;
import com.ecuatrails.api.dto.PageAdminInterestPointListResponse;
import com.ecuatrails.api.dto.StatusRequest;
import com.ecuatrails.api.dto.UpdateInterestPointRequest;
import com.ecuatrails.api.dto.UpdatePoiTransportLinkRequest;
import com.ecuatrails.api.service.AdminInterestPointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/interest-points")
@Tag(name = "Admin • InterestPoints", description = "Administración de puntos de interés (POIs)")
public class AdminInterestPointController {

	private final AdminInterestPointService service;

	public AdminInterestPointController(AdminInterestPointService service) {
		this.service = service;
	}

	@Operation(summary = "Listar POIs", description = "Devuelve una lista paginada de POIs filtrable por:\n"
			+ "- `q`: texto (nombre/ciudad/descr. según repo)\n" + "- `status`: activo/inactivo\n"
			+ "Ordenado por nombre ascendente.", operationId = "adminListPOIs")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageAdminInterestPointListResponse.class), examples = @ExampleObject(name = "page", value = "{\n"
					+ "  \"content\": [\n"
					+ "    { \"id\": 301, \"name\": \"Mirador del Cóndor\", \"city\": \"Cuenca\", \"status\": true, \"latitude\": -2.9001, \"longitude\": -79.0203 }\n"
					+ "  ],\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"totalPages\": 3,\n"
					+ "  \"totalElements\": 24\n" + "}"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (si se restringe a ADMIN)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping
	public ResponseEntity<Page<AdminInterestPointList>> list(
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Texto de búsqueda", example = "mirador") @RequestParam(required = false) String q,
			@Parameter(description = "Estado: true=activo, false=inactivo", example = "true") @RequestParam(required = false) Boolean status) {
		return ResponseEntity.ok(service.list(q, status, page, size));
	}

	@Operation(summary = "Crear POI", description = "Crea un punto de interés.", operationId = "adminCreatePOI")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Creado", content = @Content(schema = @Schema(implementation = AdminInterestPointDetail.class), examples = @ExampleObject(value = "{\n"
					+ "  \"id\": 301, \"name\": \"Mirador del Cóndor\", \"description\": \"Vista panorámica\",\n"
					+ "  \"address\": \"Km 5 vía al mirador\", \"city\": \"Cuenca\",\n"
					+ "  \"openingHours\": \"Lun-Dom 08:00-18:00\",\n" + "  \"rating\": 4.6, \"reviewCount\": 128,\n"
					+ "  \"latitude\": -2.9001, \"longitude\": -79.0203,\n" + "  \"status\": true\n" + "}"))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping
	public ResponseEntity<AdminInterestPointDetail> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Nuevo POI", content = @Content(schema = @Schema(implementation = CreateInterestPointRequest.class))) @Valid @RequestBody CreateInterestPointRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	@Operation(summary = "Actualizar POI", description = "Actualiza parcialmente un punto de interés por ID.", operationId = "adminUpdatePOI")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizado", content = @Content(schema = @Schema(implementation = AdminInterestPointDetail.class))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}")
	public ResponseEntity<AdminInterestPointDetail> update(
			@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Campos a actualizar (parciales)", content = @Content(schema = @Schema(implementation = UpdateInterestPointRequest.class))) @Valid @RequestBody UpdateInterestPointRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	@Operation(summary = "Eliminar POI", description = "Elimina el POI si no está en uso por rutas/transporte.", operationId = "adminDeletePOI")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "En uso por rutas/transporte", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Creación masiva de POIs", description = "Crea múltiples POIs en una sola petición.", operationId = "adminBulkCreatePOIs")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Creados", content = @Content(schema = @Schema(implementation = AdminInterestPointDetail.class), examples = @ExampleObject(name = "lista", value = "[\n"
					+ "  { \"id\": 301, \"name\": \"Mirador del Cóndor\", \"city\": \"Cuenca\", \"status\": true },\n"
					+ "  { \"id\": 302, \"name\": \"Cascada Azul\", \"city\": \"Cuenca\", \"status\": true }\n"
					+ "]"))),
			@ApiResponse(responseCode = "400", description = "Validación/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping(value = ":bulk")
	public ResponseEntity<List<AdminInterestPointDetail>> bulk(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Listado de POIs a crear", content = @Content(schema = @Schema(implementation = BulkInterestPointRequest.class))) @Valid @RequestBody BulkInterestPointRequest body) {
		return ResponseEntity.ok(service.bulkCreate(body));
	}

	@Operation(summary = "Geocodificar POI", description = "Obtiene lat/lng desde address+city y actualiza el POI.", operationId = "adminGeocodePOI")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GeocodeResponse.class), examples = @ExampleObject(value = "{ \"latitude\": -2.9001, \"longitude\": -79.0203 }"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Geocoding failed", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/{id}/geocode")
	public ResponseEntity<GeocodeResponse> geocode(
			@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id) {
		return ResponseEntity.ok(service.geocode(id));
	}

	@Operation(summary = "Cambiar estado (activo/inactivo)", description = "Actualiza el estado `status` del POI.", operationId = "adminSetPOIStatus")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizado", content = @Content(schema = @Schema(implementation = AdminInterestPointDetail.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/{id}/status")
	public ResponseEntity<AdminInterestPointDetail> setStatus(
			@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Nuevo estado", content = @Content(schema = @Schema(implementation = StatusRequest.class), examples = @ExampleObject(value = "{ \"status\": true }"))) @Valid @RequestBody StatusRequest body) {
		return ResponseEntity.ok(service.setStatus(id, Boolean.TRUE.equals(body.status())));
	}
	
	@Operation(summary = "Listar transportes de un POI", description = "Devuelve los vínculos POI-Transporte.", operationId = "adminListPoiTransports")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "OK",
	        content = @Content(schema = @Schema(implementation = AdminPoiTransportLinkDto.class),
	        examples = @ExampleObject(name = "lista", value = "[{\"id\": 41, \"transportId\": 7, \"transportName\": \"Bus A\", \"walkingDistanceMeters\": 200, \"estimatedWalkingTime\": 3, \"status\": true}]")))
	})
	@GetMapping("/{id}/transports")
	public ResponseEntity<java.util.List<AdminPoiTransportLinkDto>> listPoiTransports(
	        @Parameter(description = "ID del POI", example = "301") @PathVariable Integer id) {
	    return ResponseEntity.ok(service.listTransports(id));
	}

	@Operation(summary = "Vincular transporte a un POI", description = "Crea el vínculo POI-Transporte.", operationId = "adminAddPoiTransport")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Agregado",
	        content = @Content(schema = @Schema(implementation = AdminPoiTransportLinkDto.class),
	        examples = @ExampleObject(value = "{\"id\": 42, \"transportId\": 9, \"transportName\": \"Metro L1\", \"status\": true}"))),
	    @ApiResponse(responseCode = "404", description = "POI o transporte no encontrado",
	        content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
	})
	@PostMapping("/{id}/transports")
	public ResponseEntity<AdminPoiTransportLinkDto> addPoiTransport(
	        @PathVariable Integer id,
	        @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
	          description = "Vínculo a crear",
	          content = @Content(schema = @Schema(implementation = CreateIptLinkRequest.class)))
	        @Valid @RequestBody CreateIptLinkRequest body) {
	    return ResponseEntity.ok(service.addTransport(id, body));
	}

	@Operation(summary = "Actualizar vínculo POI-Transporte", description = "Edita distancia/tiempo/notas/estado del vínculo.", operationId = "adminUpdatePoiTransport")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Actualizado",
	        content = @Content(schema = @Schema(implementation = AdminPoiTransportLinkDto.class))),
	    @ApiResponse(responseCode = "404", description = "Vínculo no encontrado",
	        content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
	})
	@PutMapping("/{id}/transports/{linkId}")
	public ResponseEntity<AdminPoiTransportLinkDto> updatePoiTransport(
	        @PathVariable Integer id,
	        @PathVariable Integer linkId,
	        @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
	          description = "Campos a actualizar",
	          content = @Content(schema = @Schema(implementation = UpdatePoiTransportLinkRequest.class)))
	        @Valid @RequestBody UpdatePoiTransportLinkRequest body) {
	    return ResponseEntity.ok(service.updateTransportLink(id, linkId, body));
	}

	@Operation(summary = "Eliminar vínculo POI-Transporte", description = "Elimina el vínculo.", operationId = "adminDeletePoiTransport")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado") })
	@DeleteMapping("/{id}/transports/{linkId}")
	public ResponseEntity<Void> deletePoiTransport(
	        @PathVariable Integer id,
	        @PathVariable Integer linkId) {
	    service.removeTransport(id, linkId);
	    return ResponseEntity.noContent().build();
	}
}
