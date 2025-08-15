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

import com.ecuatrails.api.dto.AdminInterestPointDetail;
import com.ecuatrails.api.dto.AdminInterestPointList;
import com.ecuatrails.api.dto.BulkInterestPointRequest;
import com.ecuatrails.api.dto.CreateInterestPointRequest;
import com.ecuatrails.api.dto.GeocodeResponse;
import com.ecuatrails.api.dto.StatusRequest;
import com.ecuatrails.api.dto.UpdateInterestPointRequest;
import com.ecuatrails.api.service.AdminInterestPointService;

@RestController
@RequestMapping("/api/admin/interest-points")
public class AdminInterestPointController {

	private final AdminInterestPointService service;

	public AdminInterestPointController(AdminInterestPointService service) {
		this.service = service;
	}

	// GET /interest-points?page=&size=&q=&status=
	@GetMapping
	public ResponseEntity<Page<AdminInterestPointList>> list(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String q,
			@RequestParam(required = false) Boolean status) {
		return ResponseEntity.ok(service.list(q, status, page, size));
	}

	// POST /interest-points
	@PostMapping
	public ResponseEntity<AdminInterestPointDetail> create(@RequestBody CreateInterestPointRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	// PUT /interest-points/{id}
	@PutMapping("/{id}")
	public ResponseEntity<AdminInterestPointDetail> update(@PathVariable Integer id,
			@RequestBody UpdateInterestPointRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	// DELETE /interest-points/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// POST /interest-points:bulk
	@PostMapping(value = ":bulk")
	public ResponseEntity<java.util.List<AdminInterestPointDetail>> bulk(
			@RequestBody BulkInterestPointRequest body) {
		return ResponseEntity.ok(service.bulkCreate(body));
	}

	// POST /interest-points/{id}/geocode
	@PostMapping("/{id}/geocode")
	public ResponseEntity<GeocodeResponse> geocode(@PathVariable Integer id) {
		return ResponseEntity.ok(service.geocode(id));
	}

	// POST /interest-points/{id}/status
	@PostMapping("/{id}/status")
	public ResponseEntity<AdminInterestPointDetail> setStatus(@PathVariable Integer id,
			@RequestBody StatusRequest body) {
		return ResponseEntity.ok(service.setStatus(id, Boolean.TRUE.equals(body.status())));
	}
}
