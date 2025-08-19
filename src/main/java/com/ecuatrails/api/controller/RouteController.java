package com.ecuatrails.api.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.PageRouteListResponse;
import com.ecuatrails.api.dto.RouteCard;
import com.ecuatrails.api.dto.RouteDetail;
import com.ecuatrails.api.dto.RouteMap;
import com.ecuatrails.api.service.RouteService;
import com.ecuatrails.api.service.UserService;

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
@Tag(name = "Routes", description = "Listado, detalle, mapa y control de sesión de rutas")
public class RouteController {

	private final RouteService routeService;
	private final UserService userService;

	public RouteController(RouteService routeService, UserService userService) {
		this.routeService = routeService;
		this.userService = userService;
	}

	@Operation(summary = "Lista rutas", description = """
			Devuelve rutas **paginadas** con filtros opcionales:
			- `categoryId`: categoría numérica
			- `difficulty`: EASY | MEDIUM | HARD
			- `q`: búsqueda por nombre/descr.
			""", operationId = "listRoutes")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageRouteListResponse.class), examples = @ExampleObject(name = "page", value = """
					{
					  "content": [
					    {
					      "id": 205,
					      "name": "Mirador del Cóndor",
					      "description": "Ascenso con vistas panorámicas",
					      "category": "Senderismo",
					      "estimatedDuration": "PT4H",
					      "distance": 12.1,
					      "difficulty": "MEDIUM"
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
	@GetMapping("/routes")
	public ResponseEntity<org.springframework.data.domain.Page<RouteCard>> listRoutes(
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "ID de categoría", example = "1") @RequestParam(required = false) Integer categoryId,
			@Parameter(description = "Dificultad (EASY|MEDIUM|HARD)", example = "MEDIUM") @RequestParam(required = false) String difficulty,
			@Parameter(description = "Búsqueda por nombre/descr.", example = "cóndor") @RequestParam(required = false) String q) {
		return ResponseEntity.ok(routeService.list(categoryId, difficulty, q, page, size));
	}

	@Operation(summary = "Detalle de ruta", description = "Obtiene el detalle de una ruta por su ID.", operationId = "getRouteById")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RouteDetail.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/routes/{id}")
	public ResponseEntity<RouteDetail> getRoute(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id) {
		return ResponseEntity.ok(routeService.get(id));
	}

	@Operation(summary = "Mapa de ruta", description = "Devuelve puntos del trazado y alojamientos asociados (pares [lat, lng]).", operationId = "getRouteMap")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RouteMap.class), examples = @ExampleObject(value = """
					{
					  "routeId": 205,
					  "points": [[-2.9001, -79.0203], [-2.9012, -79.0187]],
					  "lodgings": [[-2.8999, -79.0220]]
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/routes/{id}/map")
	public ResponseEntity<RouteMap> getRouteMap(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id) {
		return ResponseEntity.ok(routeService.getMap(id));
	}

	@Operation(summary = "Iniciar sesión de ruta", description = "Marca el inicio de actividad en la ruta para el usuario autenticado. Cierra sesiones activas previas.", operationId = "startRoute")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta o usuario no encontrada/o", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/routes/{id}/start")
	public ResponseEntity<Void> start(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id,
			@Parameter(hidden = true) Principal principal) {
		var user = userService.findByUsername(principal.getName());
		routeService.start(id, user.getUserId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Finalizar sesión de ruta", description = "Marca como finalizada la sesión activa de la ruta para el usuario autenticado.", operationId = "finishRoute")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta o usuario no encontrada/o", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/routes/{id}/finish")
	public ResponseEntity<Void> finish(
			@Parameter(description = "ID de la ruta", example = "205") @PathVariable Integer id,
			@Parameter(hidden = true) Principal principal) {
		var user = userService.findByUsername(principal.getName());
		routeService.finish(id, user.getUserId());
		return ResponseEntity.ok().build();
	}
}
