package com.ecuatrails.api.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.RouteDetail;
import com.ecuatrails.api.dto.RouteListItem;
import com.ecuatrails.api.dto.RouteMap;
import com.ecuatrails.api.service.RouteService;
import com.ecuatrails.api.service.UserService;

@RestController
@RequestMapping("/api")
public class RouteController {

	private final RouteService routeService;
	private final UserService userService;

	public RouteController(RouteService routeService, UserService userService) {
		this.routeService = routeService;
		this.userService = userService;
	}

	// GET /routes?page=0&size=10&categoryId=&difficulty=&q=
	@GetMapping("/routes")
	public ResponseEntity<Page<RouteListItem>> listRoutes(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) String difficulty,
			@RequestParam(required = false) String q
			) {
		return ResponseEntity.ok(routeService.list(categoryId, difficulty, q, page, size));
	}

	// GET /routes/{id}
	@GetMapping("/routes/{id}")
	public ResponseEntity<RouteDetail> getRoute(@PathVariable Integer id) {
		return ResponseEntity.ok(routeService.get(id));
	}

	// GET /routes/{id}/map
	@GetMapping("/routes/{id}/map")
	public ResponseEntity<RouteMap> getRouteMap(@PathVariable Integer id) {
		return ResponseEntity.ok(routeService.getMap(id));
	}

	// POST /routes/{id}/start
	@PostMapping("/routes/{id}/start")
	public ResponseEntity<Void> start(@PathVariable Integer id, Principal principal) {
		var user = userService.findByUsername(principal.getName());
		routeService.start(id, user.getUserId());
		return ResponseEntity.ok().build();
	}

	// POST /routes/{id}/finish
	@PostMapping("/routes/{id}/finish")
	public ResponseEntity<Void> finish(@PathVariable Integer id, Principal principal) {
		var user = userService.findByUsername(principal.getName());
		routeService.finish(id, user.getUserId());
		return ResponseEntity.ok().build();
	}
}
