package com.ecuatrails.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.LodgingDetail;
import com.ecuatrails.api.dto.LodgingListItem;
import com.ecuatrails.api.dto.PageLodgingListResponse;
import com.ecuatrails.api.service.LodgingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@RequestMapping("/api")
@Tag(name = "Lodgings", description = "Alojamientos cercanos a rutas")
public class LodgingController {

	private final LodgingService lodgingService;

	public LodgingController(LodgingService lodgingService) {
		this.lodgingService = lodgingService;
	}

	@Operation(summary = "Lista alojamientos", description = """
			Devuelve una lista **paginada** de alojamientos con filtros opcionales:
			- `q`: texto libre por nombre/descripción
			- `minPrice` / `maxPrice`: límites de precio aproximado
			Ordenado por nombre ascendente.
			""", operationId = "listLodgings")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageLodgingListResponse.class), examples = @ExampleObject(name = "page", value = """
					{
					  "content": [
					    {
					      "id": 10,
					      "name": "Hostería Andina",
					      "description": "Habitaciones con vista a la montaña",
					      "approximatePrice": 45.00,
					      "latitude": -2.90,
					      "longitude": -79.02
					    }
					  ],
					  "page": 0,
					  "size": 10,
					  "totalPages": 5,
					  "totalElements": 41
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/lodgings")
	public ResponseEntity<org.springframework.data.domain.Page<LodgingListItem>> list(
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Texto de búsqueda (nombre/descr.)", example = "hostería") @RequestParam(required = false) String q,
			@Parameter(description = "Precio mínimo", example = "30.00") @RequestParam(required = false) BigDecimal minPrice,
			@Parameter(description = "Precio máximo", example = "80.00") @RequestParam(required = false) BigDecimal maxPrice) {
		return ResponseEntity.ok(lodgingService.list(q, minPrice, maxPrice, page, size));
	}

	@Operation(summary = "Detalle de alojamiento", description = "Obtiene el detalle de un alojamiento por su ID.", operationId = "getLodgingById")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LodgingDetail.class), examples = @ExampleObject(value = """
					{
					  "id": 10,
					  "name": "Hostería Andina",
					  "description": "Habitaciones con vista a la montaña",
					  "approximatePrice": 45.00,
					  "latitude": -2.90,
					  "longitude": -79.02,
					  "images": [
					    {"url":"https://cdn/acme/andina/1.jpg","alt":"Fachada"},
					    {"url":"https://cdn/acme/andina/2.jpg","alt":"Habitación doble"}
					  ],
					  "coverImage": "https://cdn/acme/andina/cover.jpg"
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Alojamiento no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/lodgings/{id}")
	public ResponseEntity<LodgingDetail> get(
			@Parameter(description = "ID del alojamiento", example = "10") @PathVariable Integer id) {
		return ResponseEntity.ok(lodgingService.get(id));
	}

	@Operation(summary = "Alojamientos por ruta", description = "Lista alojamientos activos asociados a una **ruta**.", operationId = "getLodgingsByRoute")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LodgingListItem.class), examples = @ExampleObject(name = "lista", value = """
					[
					  {
					    "id": 10,
					    "name": "Hostería Andina",
					    "description": "Habitaciones con vista a la montaña",
					    "approximatePrice": 45.00,
					    "latitude": -2.90,
					    "longitude": -79.02
					  }
					]
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/routes/{id}/lodgings")
	public ResponseEntity<List<LodgingListItem>> byRoute(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id) {
		return ResponseEntity.ok(lodgingService.byRoute(id));
	}
}
