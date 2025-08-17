package com.ecuatrails.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.InterestPointDetail;
import com.ecuatrails.api.dto.InterestPointListItem;
import com.ecuatrails.api.service.PoiService;

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
@Tag(name = "InterestPoints", description = "Puntos de interés asociados a rutas")
public class PoiController {

	private final PoiService poiService;

	public PoiController(PoiService poiService) {
		this.poiService = poiService;
	}

	@Operation(summary = "Lista puntos de interés por ruta", description = "Devuelve los puntos de interés **activos** asociados a una ruta.", operationId = "listInterestPointsByRoute")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InterestPointListItem.class), examples = @ExampleObject(name = "lista", value = """
					[
					  {
					    "id": 301,
					    "name": "Mirador del Cóndor",
					    "description": "Vista panorámica del valle",
					    "latitude": -2.9001,
					    "longitude": -79.0203
					  },
					  {
					    "id": 302,
					    "name": "Cascada Azul",
					    "description": "Pequeña cascada junto al sendero",
					    "latitude": -2.9012,
					    "longitude": -79.0187
					  }
					]
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/routes/{id}/interest-points")
	public ResponseEntity<List<InterestPointListItem>> list(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id) {
		return ResponseEntity.ok(poiService.listByRoute(id));
	}

	@Operation(summary = "Detalle de punto de interés", description = "Obtiene el detalle de un punto de interés activo por su ID.", operationId = "getInterestPointById")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InterestPointDetail.class), examples = @ExampleObject(value = """
					{
					  "id": 301,
					  "name": "Mirador del Cóndor",
					  "description": "Vista panorámica del valle",
					  "latitude": -2.9001,
					  "longitude": -79.0203,
					  "address": "Km 5 vía al mirador",
					  "city": "Cuenca",
					  "openingHours": "Lun-Dom 08:00-18:00",
					  "rating": 4.6,
					  "reviewCount": 128,
					  "images": [
					    {"url":"https://cdn/acme/poi/301/1.jpg","alt":"Acceso al mirador"},
					    {"url":"https://cdn/acme/poi/301/2.jpg","alt":"Vista del valle"}
					  ],
					  "coverImage": "https://cdn/acme/poi/301/cover.jpg"
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Punto de interés no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/interest-points/{id}")
	public ResponseEntity<InterestPointDetail> get(
			@Parameter(description = "ID del punto de interés", example = "301") @PathVariable Integer id) {
		return ResponseEntity.ok(poiService.get(id));
	}
}
