package com.ecuatrails.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ecuatrails.api.dto.CategoryDto;
import com.ecuatrails.api.dto.CreateCategoryRequest;
import com.ecuatrails.api.dto.UpdateCategoryRequest;
import com.ecuatrails.api.model.Category;
import com.ecuatrails.api.repository.CategoryRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminCategoryService {

	private final CategoryRepository categoryRepo;
	private final RouteRepository routeRepo;

	public AdminCategoryService(CategoryRepository categoryRepo, RouteRepository routeRepo) {
		this.categoryRepo = categoryRepo;
		this.routeRepo = routeRepo;
	}

	public List<CategoryDto> list() {
		return categoryRepo.findAll().stream()
				.sorted((a,b) -> a.getName().compareToIgnoreCase(b.getName()))
				.map(c -> new CategoryDto(c.getCategoryId(), c.getCode(), c.getName()))
				.collect(Collectors.toList());
	}

	public CategoryDto create(CreateCategoryRequest req) {
		validateCodeAndName(req.code(), req.name());
		if (categoryRepo.existsByCodeIgnoreCase(req.code())) throw new IllegalArgumentException("code already exists");
		if (categoryRepo.existsByNameIgnoreCase(req.name())) throw new IllegalArgumentException("name already exists");

		var c = new Category();
		c.setCode(req.code());
		c.setName(req.name());
		var saved = categoryRepo.save(c);
		return new CategoryDto(saved.getCategoryId(), saved.getCode(), saved.getName());
	}

	public CategoryDto update(Integer id, UpdateCategoryRequest req) {
		var c = categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
		if (req.code() != null) {
			if (!StringUtils.hasText(req.code())) throw new IllegalArgumentException("code required");
			if (!req.code().equalsIgnoreCase(c.getCode()) && categoryRepo.existsByCodeIgnoreCase(req.code()))
				throw new IllegalArgumentException("code already exists");
			c.setCode(req.code());
		}
		if (req.name() != null) {
			if (!StringUtils.hasText(req.name())) throw new IllegalArgumentException("name required");
			if (!req.name().equalsIgnoreCase(c.getName()) && categoryRepo.existsByNameIgnoreCase(req.name()))
				throw new IllegalArgumentException("name already exists");
			c.setName(req.name());
		}
		return new CategoryDto(c.getCategoryId(), c.getCode(), c.getName());
	}

	public void delete(Integer id) {
		var c = categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
		long inUse = routeRepo.countByCategoryId(id);
		if (inUse > 0) {
			throw new IllegalStateException("Category is in use by " + inUse + " routes");
		}
		categoryRepo.delete(c);
	}

	private void validateCodeAndName(String code, String name) {
		if (!StringUtils.hasText(code)) throw new IllegalArgumentException("code required");
		if (!StringUtils.hasText(name)) throw new IllegalArgumentException("name required");
		if (code.length() > 16) throw new IllegalArgumentException("code length > 16");
		if (name.length() > 64) throw new IllegalArgumentException("name length > 64");
	}
}
