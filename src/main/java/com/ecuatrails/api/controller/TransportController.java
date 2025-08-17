package com.ecuatrails.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.Directions;
import com.ecuatrails.api.dto.InterestPointTransportDto;
import com.ecuatrails.api.dto.RouteTransport;
import com.ecuatrails.api.service.TransportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@RequestMapping("/api")
@Tag(name = "Transport", description = "Transporte y direcciones para rutas y puntos de interés")
public class TransportController {

	private final TransportService transportService;

	public TransportController(TransportService transportService) {
		this.transportService = transportService;
	}

	@Operation(summary = "Transporte asociado a una ruta", description = "Devuelve opciones de transporte cercanas a los puntos de interés de la ruta.", operationId = "getTransportByRoute")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RouteTransport.class), examples = @ExampleObject(value = """
					{
					  "routeId": 205,
					  "items": [
					    {
					      "relationId": 9101,
					      "interestPointId": 301,
					      "interestPointName": "Mirador del Cóndor",
					      "transportId": 15,
					      "transportName": "Parada de bus - Vía Principal",
					      "walkingDistanceMeters": 450,
					      "estimatedWalkingTime": 360,
					      "accessibilityNotes": "Camino empedrado, pendiente leve"
					    }
					  ]
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/routes/{id}/transport")
	public ResponseEntity<RouteTransport> getTransportByRoute(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id) {
		return ResponseEntity.ok(transportService.getByRoute(id));
	}

	@Operation(summary = "Transporte cercano a un punto de interés", description = "Devuelve opciones de transporte asociadas a un punto de interés específico.", operationId = "getTransportByPoi")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InterestPointTransportDto.class), examples = @ExampleObject(name = "lista", value = """
					[
					  {
					    "relationId": 9101,
					    "interestPointId": 301,
					    "interestPointName": "Mirador del Cóndor",
					    "transportId": 15,
					    "transportName": "Parada de bus - Vía Principal",
					    "walkingDistanceMeters": 450,
					    "estimatedWalkingTime": 360,
					    "accessibilityNotes": "Camino empedrado, pendiente leve"
					  }
					]
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Punto de interés no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/interest-points/{id}/transport")
	public ResponseEntity<List<InterestPointTransportDto>> getTransportByPoi(
			@Parameter(description = "ID del punto de interés", example = "301") @PathVariable Integer id) {
		return ResponseEntity.ok(transportService.getByInterestPoint(id));
	}

	@Operation(summary = "Direcciones entre dos puntos", description = """
			Calcula distancia y ETA aproximados según el modo (`walking` | `driving` | `transit`) y devuelve
			una línea simple con pares **[lng, lat]** desde origen a destino.
			""", operationId = "getDirections")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Directions.class), examples = @ExampleObject(value = """
					{
					  "originLat": -2.9001,
					  "originLng": -79.0203,
					  "destLat": -2.9012,
					  "destLng": -79.0187,
					  "mode": "walking",
					  "distanceMeters": 210,
					  "etaSeconds": 168,
					  "line": [[-79.0203, -2.9001], [-79.0187, -2.9012]]
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/directions")
	public ResponseEntity<Directions> directions(
			@Parameter(description = "Latitud de origen", example = "-2.9001") @RequestParam double originLat,
			@Parameter(description = "Longitud de origen", example = "-79.0203") @RequestParam double originLng,
			@Parameter(description = "Latitud de destino", example = "-2.9012") @RequestParam double destLat,
			@Parameter(description = "Longitud de destino", example = "-79.0187") @RequestParam double destLng,
			@Parameter(description = "walking | driving | transit", example = "walking") @RequestParam(defaultValue = "walking") String mode) {
		return ResponseEntity.ok(transportService.directions(originLat, originLng, destLat, destLng, mode));
	}
}
