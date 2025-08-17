package com.ecuatrails.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.InterestPointDetail;
import com.ecuatrails.api.dto.InterestPointListItem;
import com.ecuatrails.api.helpers.Mappers;
import com.ecuatrails.api.model.InterestPoint;
import com.ecuatrails.api.repository.InterestPointRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PoiService {

	private final InterestPointRepository poiRepository;
	  private final RouteRepository routeRepository;

	  public PoiService(InterestPointRepository poiRepository, RouteRepository routeRepository) {
	    this.poiRepository = poiRepository;
	    this.routeRepository = routeRepository;
	  }

	  // GET /routes/{id}/interest-points
	  public List<InterestPointListItem> listByRoute(Integer routeId) {
	    routeRepository.findById(routeId).orElseThrow(() -> new NoSuchElementException("Route not found"));
	    return poiRepository.findActiveByRoute(routeId).stream()
	        .map(Mappers::toInterestPointListItem).collect(Collectors.toList());
	  }

	  // GET /interest-points/{id}
	  public InterestPointDetail get(Integer id) {
	    InterestPoint p = poiRepository.findActiveById(id)
	        .orElseThrow(() -> new NoSuchElementException("Interest point not found"));
	    return Mappers.toInterestPointDetail(p);
	  }
}
