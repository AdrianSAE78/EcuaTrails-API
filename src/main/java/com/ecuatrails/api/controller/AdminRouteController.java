package com.ecuatrails.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.AdminRouteDetailDto;
import com.ecuatrails.api.dto.AdminRouteListDto;
import com.ecuatrails.api.dto.AdminRouteLodgingLinkDto;
import com.ecuatrails.api.dto.AdminRoutePoiLinkDto;
import com.ecuatrails.api.dto.CreateRouteLodgingLinkRequest;
import com.ecuatrails.api.dto.CreateRoutePoiLinkRequest;
import com.ecuatrails.api.dto.CreateRouteRequest;
import com.ecuatrails.api.dto.ReorderRoutePoiRequest;
import com.ecuatrails.api.dto.UpdateRouteRequest;
import com.ecuatrails.api.service.AdminRouteService;

@RestController
@RequestMapping("/api/admin/routes")
public class AdminRouteController {

	private final AdminRouteService service;

	public AdminRouteController(AdminRouteService service) {
		this.service = service;
	}

	// --- Routes ---

	// GET /routes?page=&size=&q=&categoryId=&status=
	@GetMapping
	public ResponseEntity<Page<AdminRouteListDto>> list(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String q,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Boolean status) {
		return ResponseEntity.ok(service.list(q, categoryId, status, page, size));
	}

	// POST /routes
	@PostMapping
	public ResponseEntity<AdminRouteDetailDto> create(@RequestBody CreateRouteRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	// PUT /routes/{id}
	@PutMapping("/{id}")
	public ResponseEntity<AdminRouteDetailDto> update(@PathVariable Integer id, @RequestBody UpdateRouteRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	// DELETE /routes/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// --- POIs ---

	// GET /routes/{id}/interest-points
	@GetMapping("/{id}/interest-points")
	public ResponseEntity<java.util.List<AdminRoutePoiLinkDto>> listPois(@PathVariable Integer id) {
		return ResponseEntity.ok(service.listPois(id));
	}

	// POST /routes/{id}/interest-points
	@PostMapping("/{id}/interest-points")
	public ResponseEntity<AdminRoutePoiLinkDto> addPoi(@PathVariable Integer id,
			@RequestBody CreateRoutePoiLinkRequest body) {
		return ResponseEntity.ok(service.addPoi(id, body));
	}

	// DELETE /routes/{id}/interest-points/{routeInterestPointId}
	@DeleteMapping("/{id}/interest-points/{routeInterestPointId}")
	public ResponseEntity<Void> deletePoi(@PathVariable Integer id, @PathVariable Integer routeInterestPointId) {
		service.deletePoi(id, routeInterestPointId);
		return ResponseEntity.noContent().build();
	}

	// PUT /routes/{id}/interest-points/reorder
	@PutMapping("/{id}/interest-points/reorder")
	public ResponseEntity<java.util.List<AdminRoutePoiLinkDto>> reorderPois(@PathVariable Integer id,
			@RequestBody ReorderRoutePoiRequest body) {
		return ResponseEntity.ok(service.reorderPois(id, body));
	}

	// --- Lodgings ---

	// GET /routes/{id}/lodgings
	@GetMapping("/{id}/lodgings")
	public ResponseEntity<java.util.List<AdminRouteLodgingLinkDto>> listLodgings(@PathVariable Integer id) {
		return ResponseEntity.ok(service.listLodgings(id));
	}

	// POST /routes/{id}/lodgings
	@PostMapping("/{id}/lodgings")
	public ResponseEntity<AdminRouteLodgingLinkDto> addLodging(@PathVariable Integer id,
			@RequestBody CreateRouteLodgingLinkRequest body) {
		return ResponseEntity.ok(service.addLodging(id, body));
	}

	// DELETE /routes/{id}/lodgings/{routeLodgingId}
	@DeleteMapping("/{id}/lodgings/{routeLodgingId}")
	public ResponseEntity<Void> deleteLodging(@PathVariable Integer id, @PathVariable Integer routeLodgingId) {
		service.deleteLodging(id, routeLodgingId);
		return ResponseEntity.noContent().build();
	}
}
