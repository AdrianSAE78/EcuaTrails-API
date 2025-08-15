package com.ecuatrails.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.LodgingDetail;
import com.ecuatrails.api.dto.LodgingListItem;
import com.ecuatrails.api.helpers.converter.Mappers;
import com.ecuatrails.api.model.Lodging;
import com.ecuatrails.api.repository.LodgingRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LodgingService {

	private final LodgingRepository lodgingRepo;
	  private final RouteRepository routeRepo;

	  public LodgingService(LodgingRepository lodgingRepo, RouteRepository routeRepo) {
	    this.lodgingRepo = lodgingRepo;
	    this.routeRepo = routeRepo;
	  }

	  public Page<LodgingListItem> list(String q, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
	    var pageable = PageRequest.of(page, size, Sort.by("name").ascending());
	    return lodgingRepo.search(q, minPrice, maxPrice, pageable).map(Mappers::toLodgingListItem);
	  }

	  public LodgingDetail get(Integer id) {
	    Lodging l = lodgingRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Lodging not found"));
	    return Mappers.toLodgingDetail(l);
	  }

	  public List<LodgingListItem> byRoute(Integer routeId) {
	    routeRepo.findById(routeId).orElseThrow(() -> new NoSuchElementException("Route not found"));
	    return routeRepo.findActiveLodgingsByRoute(routeId).stream()
	        .map(Mappers::toLodgingListItem)
	        .collect(Collectors.toList());
	  }
}
