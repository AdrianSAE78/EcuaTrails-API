package com.ecuatrails.api.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.CreateHistoryRequest;
import com.ecuatrails.api.dto.HistoryItem;
import com.ecuatrails.api.dto.PageHistoryItemResponse;
import com.ecuatrails.api.service.HistoryService;
import com.ecuatrails.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@RequestMapping("/api")
@Tag(name = "History", description = "Historial de rutas del usuario (listar, crear, eliminar, repetir)")
public class HistoryController {

	private final HistoryService historyService;
	private final UserService userService;

	public HistoryController(HistoryService historyService, UserService userService) {
		this.historyService = historyService;
		this.userService = userService;
	}

	@Operation(summary = "Lista historial del usuario", description = """
			Devuelve el historial **paginado** del usuario autenticado (visitas/realizaciones de rutas).
			Requiere que el `id` del path coincida con el usuario autenticado.
			""", operationId = "listUserHistory")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageHistoryItemResponse.class), examples = @ExampleObject(name = "page", value = """
					{
					  "content": [
					    {
					      "historyId": 31,
					      "routeId": 205,
					      "routeName": "Mirador del Cóndor",
					      "difficulty": "MEDIUM",
					      "estimatedDuration": "PT4H",
					      "routeDate": "2025-08-12T10:15:00",
					      "isFinished": true
					    }
					  ],
					  "page": 0,
					  "size": 10,
					  "totalPages": 2,
					  "totalElements": 17
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "Prohibido (intenta leer historial de otro usuario)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/users/{id}/history")
	public ResponseEntity<org.springframework.data.domain.Page<HistoryItem>> getHistory(
			@Parameter(description = "ID del usuario (debe ser el mismo que el autenticado)", example = "42") @PathVariable Integer id,
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id))
			return ResponseEntity.status(403).build();
		return ResponseEntity.ok(historyService.list(id, page, size));
	}

	@Operation(summary = "Añade un registro al historial", description = """
			Crea un registro de historial para el usuario. Si `routeDate` no se envía, se usa la hora actual.
			Si `isFinished` es null, por defecto `false`. Requiere que `id` coincida con el usuario autenticado.
			""", operationId = "createHistory")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Creado", content = @Content(schema = @Schema(implementation = HistoryItem.class), examples = @ExampleObject(value = """
					{
					  "historyId": 32,
					  "routeId": 205,
					  "routeName": "Mirador del Cóndor",
					  "difficulty": "MEDIUM",
					  "estimatedDuration": "PT4H",
					  "routeDate": "2025-08-16T09:30:00",
					  "isFinished": false
					}
					"""))),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "Prohibido (intenta crear en otro usuario)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Usuario o ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/users/{id}/history")
	public ResponseEntity<HistoryItem> addHistory(
			@Parameter(description = "ID del usuario (debe ser el mismo que el autenticado)", example = "42") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del registro de historial", content = @Content(schema = @Schema(implementation = CreateHistoryRequest.class), examples = @ExampleObject(value = """
					{ "routeId": 205, "isFinished": false, "routeDate": "2025-08-16T09:30:00" }
					"""))) @Valid @RequestBody CreateHistoryRequest body,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id))
			return ResponseEntity.status(403).build();
		return ResponseEntity.ok(historyService.create(id, body));
	}

	@Operation(summary = "Elimina un registro del historial", description = """
			Elimina un registro del historial del usuario. Requiere que `id` coincida con el usuario autenticado.
			Devuelve **204** si se elimina correctamente.
			""", operationId = "deleteHistory")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "Prohibido (intenta eliminar de otro usuario)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Historial no encontrado para el usuario", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/users/{id}/history/{historyId}")
	public ResponseEntity<Void> deleteHistory(
			@Parameter(description = "ID del usuario (debe ser el mismo que el autenticado)", example = "42") @PathVariable Integer id,
			@Parameter(description = "ID del registro de historial a eliminar", example = "31") @PathVariable Integer historyId,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id))
			return ResponseEntity.status(403).build();
		historyService.delete(id, historyId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Repetir una ruta", description = """
			Crea un nuevo registro en el historial para la **ruta** indicada, con `routeDate=now` y `isFinished=false`.
			Usa el usuario autenticado (no requiere `userId` en el path).
			""", operationId = "repeatRoute")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK (registro creado)", content = @Content(schema = @Schema(implementation = HistoryItem.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/routes/{id}/repeat")
	public ResponseEntity<HistoryItem> repeat(
			@Parameter(description = "ID de la ruta a repetir", example = "205") @PathVariable Integer id,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		return ResponseEntity.ok(historyService.repeatRoute(me.getUserId(), id));
	}
}
