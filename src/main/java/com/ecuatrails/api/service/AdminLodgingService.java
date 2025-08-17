package com.ecuatrails.api.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.AdminLodgingDetailDto;
import com.ecuatrails.api.dto.AdminLodgingListDto;
import com.ecuatrails.api.dto.CreateLodgingRequest;
import com.ecuatrails.api.dto.UpdateLodgingRequest;
import com.ecuatrails.api.helpers.Mappers;
import com.ecuatrails.api.model.Lodging;
import com.ecuatrails.api.repository.LodgingRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminLodgingService {

	private final LodgingRepository lodgingRepo;
	private final RouteRepository routeRepo;

	public AdminLodgingService(LodgingRepository lodgingRepo, RouteRepository routeRepo) {
		this.lodgingRepo = lodgingRepo;
		this.routeRepo = routeRepo;
	}

	// GET /lodgings
	public Page<AdminLodgingListDto> list(String q, Boolean status, java.math.BigDecimal minPrice,
			java.math.BigDecimal maxPrice, int page, int size) {
		var pageable = PageRequest.of(page, size);
		return lodgingRepo.searchByStatus(q, status, minPrice, maxPrice, pageable).map(Mappers::toAdminLodgingList);
	}

	// POST /lodgings
	public AdminLodgingDetailDto create(CreateLodgingRequest r) {
		var l = new Lodging();
		Mappers.applyAdminLodgingCreate(l, r);
		return Mappers.toAdminLodgingDetail(lodgingRepo.save(l));
	}

	// PUT /lodgings/{id}
	public AdminLodgingDetailDto update(Integer id, UpdateLodgingRequest r) {
		var l = lodgingRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Lodging not found"));
		Mappers.applyAdminLodgingUpdate(l, r);
		return Mappers.toAdminLodgingDetail(l);
	}

	// DELETE /lodgings/{id}
	public void delete(Integer id) {
		lodgingRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Lodging not found"));
		long usedByRoutes = routeRepo.countRoutesUsingLodging(id); // usa join many-to-many.
																	// :contentReference[oaicite:6]{index=6}
		if (usedByRoutes > 0) {
			throw new IllegalStateException("Lodging is used by " + usedByRoutes + " routes");
		}
		lodgingRepo.deleteById(id);
	}
}
