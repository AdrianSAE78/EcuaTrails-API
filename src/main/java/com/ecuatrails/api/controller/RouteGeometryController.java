package com.ecuatrails.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.repository.RouteInterestPointRepository;
import com.ecuatrails.api.repository.RouteRepository;

@RestController
@RequestMapping("/api/routes")
public class RouteGeometryController {

	private final RouteRepository routeRepo;
	private final RouteInterestPointRepository ripRepo;

	public RouteGeometryController(RouteRepository routeRepo, RouteInterestPointRepository ripRepo) {
		this.routeRepo = routeRepo;
		this.ripRepo = ripRepo;
	}

	@GetMapping("/{id}/geometry")
	public Map<String, Object> geometry(@PathVariable Integer id) {
		var route = routeRepo.findById(id).orElseThrow();

		var rips = ripRepo.listByRoute(id);

		var coords = rips.stream().map(rip -> List.of(rip.getInterestPoint().getLongitude().doubleValue(),
				rip.getInterestPoint().getLatitude().doubleValue())).toList();

		return Map.of("type", "FeatureCollection", "features",
				List.of(Map.of("type", "Feature", "geometry", Map.of("type", "LineString", "coordinates", coords),
						"properties", Map.of("routeId", route.getRouteId(), "name", route.getName()))));
	}
}
