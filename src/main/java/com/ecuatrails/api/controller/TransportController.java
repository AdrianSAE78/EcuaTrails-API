package com.ecuatrails.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.Directions;
import com.ecuatrails.api.dto.InterestPointTransportDto;
import com.ecuatrails.api.dto.RouteTransport;
import com.ecuatrails.api.service.TransportService;

@RestController
@RequestMapping("/api")
public class TransportController {

	private final TransportService transportService;

	public TransportController(TransportService transportService) {
		this.transportService = transportService;
	}

	// GET /routes/{id}/transport
	@GetMapping("/routes/{id}/transport")
	public ResponseEntity<RouteTransport> getTransportByRoute(@PathVariable Integer id) {
		return ResponseEntity.ok(transportService.getByRoute(id));
	}

	// GET /interest-points/{id}/transport
	@GetMapping("/interest-points/{id}/transport")
	public ResponseEntity<java.util.List<InterestPointTransportDto>> getTransportByPoi(@PathVariable Integer id) {
		return ResponseEntity.ok(transportService.getByInterestPoint(id));
	}

	// GET /directions?originLat=&originLng=&destLat=&destLng=&mode=
	@GetMapping("/directions")
	public ResponseEntity<Directions> directions(
			@RequestParam double originLat,
			@RequestParam double originLng,
			@RequestParam double destLat,
			@RequestParam double destLng,
			@RequestParam(defaultValue = "walking") String mode
			) {
		return ResponseEntity.ok(transportService.directions(originLat, originLng, destLat, destLng, mode));
	}
}
