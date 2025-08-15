package com.ecuatrails.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.CategoryDto;
import com.ecuatrails.api.dto.RouteCard;
import com.ecuatrails.api.helpers.converter.Mappers;
import com.ecuatrails.api.repository.CategoryRepository;
import com.ecuatrails.api.service.RecommendationService;

@RestController
@RequestMapping("/api")
public class DashboardController {
	private final CategoryRepository categoryRepository;
    private final RecommendationService recommendationService;

    public DashboardController(CategoryRepository categoryRepository,
                               RecommendationService recommendationService) {
        this.categoryRepository = categoryRepository;
        this.recommendationService = recommendationService;
    }

    // GET /categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> out = categoryRepository.findAllByOrderByNameAsc()
            .stream().map(Mappers::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    // GET /routes/recommended?userId=
    @GetMapping("/routes/recommended")
    public ResponseEntity<List<RouteCard>> getRecommended(@RequestParam Integer userId) {
        return ResponseEntity.ok(recommendationService.recommendedForUser(userId));
    }
}
