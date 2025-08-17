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

import com.ecuatrails.api.dto.AddFavoriteRequest;
import com.ecuatrails.api.dto.FavoriteItemDto;
import com.ecuatrails.api.dto.PageFavoriteItemResponse;
import com.ecuatrails.api.service.FavoriteService;
import com.ecuatrails.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@RequestMapping("/api")
@Tag(name = "Favorites", description = "Gestión de rutas favoritas del usuario")
public class FavoriteController {

	private final FavoriteService favoriteService;
	private final UserService userService;

	public FavoriteController(FavoriteService favoriteService, UserService userService) {
		this.favoriteService = favoriteService;
		this.userService = userService;
	}

	@Operation(summary = "Lista favoritos del usuario", description = """
			Devuelve la lista paginada de rutas favoritas **del propio usuario**.
			Requiere que el `id` del path coincida con el usuario autenticado.
			""", operationId = "listUserFavorites")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageFavoriteItemResponse.class), examples = @ExampleObject(name = "page", value = """
					{
					  "content": [
					    {
					      "favoriteId": 11,
					      "routeId": 101,
					      "routeName": "Laguna Encantada",
					      "difficulty": "EASY",
					      "estimatedDuration": "PT2H30M",
					      "created": "2025-08-10T14:22:11"
					    }
					  ],
					  "page": 0,
					  "size": 10,
					  "totalPages": 3,
					  "totalElements": 21
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "Prohibido (intenta leer favoritos de otro usuario)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/users/{id}/favorites")
	public ResponseEntity<org.springframework.data.domain.Page<FavoriteItemDto>> list(
			@Parameter(description = "ID del usuario (debe ser el mismo que el autenticado)", example = "42") @PathVariable Integer id,
			@Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id))
			return ResponseEntity.status(403).build();
		return ResponseEntity.ok(favoriteService.list(id, page, size));
	}

	@Operation(summary = "Añade una ruta a favoritos", description = """
			Marca una ruta como favorita para el usuario. **Idempotente**:
			si ya estaba en favoritos, devuelve el favorito existente.
			Requiere que `id` coincida con el usuario autenticado.
			""", operationId = "addFavorite")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK (creado o ya existente)", content = @Content(schema = @Schema(implementation = FavoriteItemDto.class), examples = @ExampleObject(value = """
					{
					  "favoriteId": 12,
					  "routeId": 205,
					  "routeName": "Mirador del Cóndor",
					  "difficulty": "MEDIUM",
					  "estimatedDuration": "PT4H",
					  "created": "2025-08-15T09:05:31"
					}
					"""))),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "Prohibido (intenta crear en otro usuario)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Usuario o ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PostMapping("/users/{id}/favorites")
	public ResponseEntity<FavoriteItemDto> add(
			@Parameter(description = "ID del usuario (debe ser el mismo que el autenticado)", example = "42") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Ruta a marcar como favorita", content = @Content(schema = @Schema(implementation = AddFavoriteRequest.class), examples = @ExampleObject(value = """
					{ "routeId": 205 }
					"""))) @Valid @RequestBody AddFavoriteRequest body,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id))
			return ResponseEntity.status(403).build();
		return ResponseEntity.ok(favoriteService.add(id, body));
	}

	@Operation(summary = "Elimina un favorito", description = """
			Elimina un favorito del usuario. Requiere que `id` coincida con el usuario autenticado.
			Devuelve **204** si se elimina correctamente.
			""", operationId = "deleteFavorite")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminado"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "Prohibido (intenta eliminar de otro usuario)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Favorito no encontrado para el usuario", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/users/{id}/favorites/{favoriteId}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID del usuario (debe ser el mismo que el autenticado)", example = "42") @PathVariable Integer id,
			@Parameter(description = "ID del favorito a eliminar", example = "12") @PathVariable Integer favoriteId,
			Principal principal) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id))
			return ResponseEntity.status(403).build();
		favoriteService.delete(id, favoriteId);
		return ResponseEntity.noContent().build();
	}
}
