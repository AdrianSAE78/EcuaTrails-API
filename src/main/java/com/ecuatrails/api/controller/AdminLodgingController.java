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

import com.ecuatrails.api.dto.AdminLodgingDetailDto;
import com.ecuatrails.api.dto.AdminLodgingListDto;
import com.ecuatrails.api.dto.CreateLodgingRequest;
import com.ecuatrails.api.dto.UpdateLodgingRequest;
import com.ecuatrails.api.service.AdminLodgingService;

@RestController
@RequestMapping("/api/admin/lodgings")
public class AdminLodgingController {

	private final AdminLodgingService service;

	public AdminLodgingController(AdminLodgingService service) {
		this.service = service;
	}

	// GET /lodgings?page=&size=&q=&status=&minPrice=&maxPrice=
	@GetMapping
	public ResponseEntity<Page<AdminLodgingListDto>> list(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String q,
			@RequestParam(required = false) Boolean status,
			@RequestParam(required = false) java.math.BigDecimal minPrice,
			@RequestParam(required = false) java.math.BigDecimal maxPrice) {
		return ResponseEntity.ok(service.list(q, status, minPrice, maxPrice, page, size));
	}

	// POST /lodgings
	@PostMapping
	public ResponseEntity<AdminLodgingDetailDto> create(@RequestBody CreateLodgingRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	// PUT /lodgings/{id}
	@PutMapping("/{id}")
	public ResponseEntity<AdminLodgingDetailDto> update(@PathVariable Integer id,
			@RequestBody UpdateLodgingRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	// DELETE /lodgings/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}