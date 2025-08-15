package com.ecuatrails.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.LodgingDetail;
import com.ecuatrails.api.dto.LodgingListItem;
import com.ecuatrails.api.service.LodgingService;

@RestController
@RequestMapping("/api")
public class LodgingController {

	private final LodgingService lodgingService;

	public LodgingController(LodgingService lodgingService) {
		this.lodgingService = lodgingService;
	}

	// GET /lodgings?page=0&size=10&q=&minPrice=&maxPrice=
	@GetMapping("/lodgings")
	public ResponseEntity<Page<LodgingListItem>> list(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String q,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice
			) {
		return ResponseEntity.ok(lodgingService.list(q, minPrice, maxPrice, page, size));
	}

	// GET /lodgings/{id}
	@GetMapping("/lodgings/{id}")
	public ResponseEntity<LodgingDetail> get(@PathVariable Integer id) {
		return ResponseEntity.ok(lodgingService.get(id));
	}

	// GET /routes/{id}/lodgings
	@GetMapping("/routes/{id}/lodgings")
	public ResponseEntity<List<LodgingListItem>> byRoute(@PathVariable Integer id) {
		return ResponseEntity.ok(lodgingService.byRoute(id));
	}
}
