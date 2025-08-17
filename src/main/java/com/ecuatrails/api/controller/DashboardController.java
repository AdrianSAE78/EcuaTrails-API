package com.ecuatrails.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.ApiError;
import com.ecuatrails.api.dto.CategoryDto;
import com.ecuatrails.api.dto.RouteCard;
import com.ecuatrails.api.helpers.Mappers;
import com.ecuatrails.api.repository.CategoryRepository;
import com.ecuatrails.api.service.RecommendationService;

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
@Tag(name = "Dashboard", description = "Recursos de inicio (categorías y rutas recomendadas)")
public class DashboardController {
	private final CategoryRepository categoryRepository;
	private final RecommendationService recommendationService;

	public DashboardController(CategoryRepository categoryRepository, RecommendationService recommendationService) {
		this.categoryRepository = categoryRepository;
		this.recommendationService = recommendationService;
	}

	@Operation(summary = "Lista categorías", description = "Devuelve todas las categorías ordenadas ascendentemente por nombre.", operationId = "getCategories")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CategoryDto.class), examples = @ExampleObject(name = "lista", value = """
					[
					  {"id":1,"code":"HIKING","name":"Senderismo"},
					  {"id":2,"code":"BIKE","name":"Ciclismo"}
					]
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDto>> getCategories() {
		List<CategoryDto> out = categoryRepository.findAllByOrderByNameAsc().stream().map(Mappers::toCategoryDto)
				.collect(Collectors.toList());
		return ResponseEntity.ok(out);
	}

	@Operation(summary = "Rutas recomendadas para un usuario", description = "Recomienda rutas considerando preferencias del usuario (categoría preferida, duración máxima) "
			+ "y excluyendo rutas finalizadas en los últimos 30 días.", operationId = "getRecommendedRoutes")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RouteCard.class), examples = @ExampleObject(name = "lista", value = """
					[
					  {
					    "id":101,
					    "name":"Laguna Encantada",
					    "description":"Circuito alrededor de la laguna",
					    "category":"Senderismo",
					    "estimatedDuration":"PT2H30M",
					    "distance":7.8,
					    "difficulty":"EASY"
					  },
					  {
					    "id":205,
					    "name":"Mirador del Cóndor",
					    "description":"Ascenso con vistas panorámicas",
					    "category":"Senderismo",
					    "estimatedDuration":"PT4H",
					    "distance":12.1,
					    "difficulty":"MEDIUM"
					  }
					]
					"""))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/routes/recommended")
	public ResponseEntity<List<RouteCard>> getRecommended(
			@Parameter(description = "ID del usuario para personalizar recomendaciones", example = "42") @RequestParam Integer userId) {
		return ResponseEntity.ok(recommendationService.recommendedForUser(userId));
	}
}
