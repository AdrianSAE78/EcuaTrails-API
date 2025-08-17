package com.ecuatrails.api.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.MeDto;
import com.ecuatrails.api.dto.MeStatsDto;
import com.ecuatrails.api.dto.Preference;
import com.ecuatrails.api.dto.UpdateMeRequest;
import com.ecuatrails.api.dto.UpdatePreferenceRequest;
import com.ecuatrails.api.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@RequestMapping("/api/users/me")
@Tag(name = "Me", description = "Perfil del usuario autenticado (datos, preferencias y estadísticas)")
public class MeController {

	private final ProfileService profileService;

	public MeController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@Operation(summary = "Obtiene mi perfil", description = "Devuelve los datos del usuario autenticado.", operationId = "getMe")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MeDto.class), examples = @ExampleObject(value = """
					{
					  "id": 42,
					  "name": "Ana",
					  "lastName": "Pérez",
					  "username": "ana",
					  "email": "ana@acme.com",
					  "birthday": "1995-03-15",
					  "authProvider": "LOCAL",
					  "uid": "e4b8a1..."
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping
	public ResponseEntity<MeDto> getMe(@Parameter(hidden = true) Principal principal) {
		return ResponseEntity.ok(profileService.getMe(principal.getName()));
	}

	@Operation(summary = "Actualiza mi perfil", description = "Actualiza campos básicos del perfil. Los campos no enviados permanecen iguales.", operationId = "updateMe")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizado", content = @Content(schema = @Schema(implementation = MeDto.class))),
			@ApiResponse(responseCode = "400", description = "Validación fallida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping
	public ResponseEntity<MeDto> updateMe(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Campos a actualizar (parciales)", content = @Content(schema = @Schema(implementation = UpdateMeRequest.class), examples = @ExampleObject(value = """
					{ "name": "Ana María", "email": "ana.maria@acme.com" }
					"""))) @Valid @RequestBody UpdateMeRequest body,
			@Parameter(hidden = true) Principal principal) {
		return ResponseEntity.ok(profileService.updateMe(principal.getName(), body));
	}

	@Operation(summary = "Obtiene mis preferencias", description = "Devuelve las preferencias de recomendación del usuario autenticado.", operationId = "getMyPreferences")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Preference.class), examples = @ExampleObject(value = """
					{
					  "categoryId": 1,
					  "preferedBudget": 80.00,
					  "preferedDuration": "PT3H"
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/preferences")
	public ResponseEntity<Preference> getPreferences(@Parameter(hidden = true) Principal principal) {
		return ResponseEntity.ok(profileService.getPreferences(principal.getName()));
	}

	@Operation(summary = "Actualiza mis preferencias", description = """
			Actualiza preferencias de recomendación (categoría, presupuesto y duración).
			Los campos no enviados permanecen iguales.
			""", operationId = "updateMyPreferences")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizado", content = @Content(schema = @Schema(implementation = Preference.class))),
			@ApiResponse(responseCode = "400", description = "Validación fallida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/preferences")
	public ResponseEntity<Preference> updatePreferences(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Preferencias a actualizar (parciales)", content = @Content(schema = @Schema(implementation = UpdatePreferenceRequest.class), examples = @ExampleObject(value = """
					{ "categoryId": 1, "preferedBudget": 100.00, "preferedDuration": "PT2H30M" }
					"""))) @Valid @RequestBody UpdatePreferenceRequest body,
			@Parameter(hidden = true) Principal principal) {
		return ResponseEntity.ok(profileService.updatePreferences(principal.getName(), body));
	}

	@Operation(summary = "Obtiene mis estadísticas", description = "Métricas agregadas del usuario (rutas finalizadas, distancia total, duración total, última actividad).", operationId = "getMyStats")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MeStatsDto.class), examples = @ExampleObject(value = """
					{
					  "totalFinished": 18,
					  "totalDistanceKm": 124.6,
					  "totalDurationMinutes": 980,
					  "lastActivityAt": "2025-08-14T18:05:00"
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/stats")
	public ResponseEntity<MeStatsDto> getStats(@Parameter(hidden = true) Principal principal) {
		return ResponseEntity.ok(profileService.getStats(principal.getName()));
	}
}
