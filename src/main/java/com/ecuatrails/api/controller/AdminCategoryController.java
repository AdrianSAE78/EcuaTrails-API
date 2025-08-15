package com.ecuatrails.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.CategoryDto;
import com.ecuatrails.api.dto.CreateCategoryRequest;
import com.ecuatrails.api.dto.UpdateCategoryRequest;
import com.ecuatrails.api.service.AdminCategoryService;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

	private final AdminCategoryService service;

	public AdminCategoryController(AdminCategoryService service) {
		this.service = service;
	}

	// GET /categories
	@GetMapping
	public ResponseEntity<List<CategoryDto>> list() {
		return ResponseEntity.ok(service.list());
	}

	// POST /categories
	@PostMapping
	public ResponseEntity<CategoryDto> create(@RequestBody CreateCategoryRequest body) {
		return ResponseEntity.ok(service.create(body));
	}

	// PUT /categories/{id}
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> update(@PathVariable Integer id, @RequestBody UpdateCategoryRequest body) {
		return ResponseEntity.ok(service.update(id, body));
	}

	// DELETE /categories/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
