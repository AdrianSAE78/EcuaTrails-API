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

import com.ecuatrails.api.dto.AdminIptDto;
import com.ecuatrails.api.dto.AdminTransportDetail;
import com.ecuatrails.api.dto.AdminTransportList;
import com.ecuatrails.api.dto.CreateIptLinkRequest;
import com.ecuatrails.api.dto.CreateTransportRequest;
import com.ecuatrails.api.dto.UpdateTransportRequest;
import com.ecuatrails.api.service.AdminTransportService;

@RestController
@RequestMapping("/api/admin")
public class AdminTransportController {

	private final AdminTransportService service;

	public AdminTransportController(AdminTransportService service) {
		this.service = service;
	}

	// GET /transport?page=&size=&q=&type=&status=
	@GetMapping("/transport")
	public ResponseEntity<Page<AdminTransportList>> listTransport(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String q,
			@RequestParam(required = false) String type, @RequestParam(required = false) Boolean status) {
		return ResponseEntity.ok(service.list(q, type, status, page, size));
	}

	// POST /transport
	@PostMapping("/transport")
	public ResponseEntity<AdminTransportDetail> createTransport(@RequestBody CreateTransportRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	// PUT /transport/{id}
	@PutMapping("/transport/{id}")
	public ResponseEntity<AdminTransportDetail> updateTransport(@PathVariable Integer id,
			@RequestBody UpdateTransportRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	// DELETE /transport/{id}
	@DeleteMapping("/transport/{id}")
	public ResponseEntity<Void> deleteTransport(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// GET /interest-points/{id}/transport
	@GetMapping("/interest-points/{id}/transport")
	public ResponseEntity<java.util.List<AdminIptDto>> listByPoi(@PathVariable Integer id) {
		return ResponseEntity.ok(service.listByPoi(id));
	}

	// POST /interest-points/{id}/transport
	@PostMapping("/interest-points/{id}/transport")
	public ResponseEntity<AdminIptDto> link(@PathVariable Integer id, @RequestBody CreateIptLinkRequest body) {
		return ResponseEntity.ok(service.link(id, body));
	}

	// DELETE /interest-points/{id}/transport/{interestPointTransportId}
	@DeleteMapping("/interest-points/{id}/transport/{interestPointTransportId}")
	public ResponseEntity<Void> unlink(@PathVariable Integer id, @PathVariable Integer interestPointTransportId) {
		service.unlink(id, interestPointTransportId);
		return ResponseEntity.noContent().build();
	}
}
