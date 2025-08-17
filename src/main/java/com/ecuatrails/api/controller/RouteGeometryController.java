package com.ecuatrails.api.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.GeoJsonFeature;
import com.ecuatrails.api.dto.GeoJsonFeatureCollection;
import com.ecuatrails.api.dto.LineStringGeometry;
import com.ecuatrails.api.repository.RouteInterestPointRepository;
import com.ecuatrails.api.repository.RouteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@RestController
@RequestMapping("/api/routes")
@Tag(name = "Route Geometry", description = "Geometrías de rutas en formato GeoJSON (WGS84)")
public class RouteGeometryController {

	private final RouteRepository routeRepo;
	private final RouteInterestPointRepository ripRepo;

	public RouteGeometryController(RouteRepository routeRepo, RouteInterestPointRepository ripRepo) {
		this.routeRepo = routeRepo;
		this.ripRepo = ripRepo;
	}

	@Operation(summary = "GeoJSON de la ruta", description = """
			Devuelve un **FeatureCollection** con un único Feature:
			- geometry: **LineString** con coordenadas **[lng, lat]** (WGS84).
			- properties: `routeId`, `name`.
			""", operationId = "getRouteGeometry")
	@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/geo+json", schema = @Schema(implementation = GeoJsonFeatureCollection.class), examples = @ExampleObject(value = """
			{
			  "type": "FeatureCollection",
			  "features": [
			    {
			      "type": "Feature",
			      "geometry": {
			        "type": "LineString",
			        "coordinates": [
			          [-79.0203, -2.9001],
			          [-79.0187, -2.9012]
			        ]
			      },
			      "properties": {
			        "routeId": 205,
			        "name": "Mirador del Cóndor"
			      }
			    }
			  ]
			}
			""")))
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
	@ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
	@GetMapping(value = "/{id}/geometry", produces = "application/geo+json")
	public ResponseEntity<GeoJsonFeatureCollection> geometry(@PathVariable Integer id) {
		var route = routeRepo.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Route not found"));
		var rips = ripRepo.listByRoute(id);

		// Importante: orden [lng, lat]
		var coords = rips.stream().map(rip -> new double[] { rip.getInterestPoint().getLongitude().doubleValue(),
				rip.getInterestPoint().getLatitude().doubleValue() }).toList();

		var line = new LineStringGeometry("LineString", coords);
		var props = Map.<String, Object>ofEntries(Map.entry("routeId", route.getRouteId()),
				Map.entry("name", route.getName()));

		var feature = new GeoJsonFeature("Feature", line, props);
		var fc = new GeoJsonFeatureCollection("FeatureCollection", java.util.List.of(feature));

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/geo+json")).body(fc);
	}
}
