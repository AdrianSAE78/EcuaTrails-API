package com.ecuatrails.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecuatrails.api.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findAllByOrderByNameAsc();
	boolean existsByCodeIgnoreCase(String code);
	boolean existsByNameIgnoreCase(String name);
}
