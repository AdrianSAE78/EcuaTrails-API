package com.ecuatrails.api.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.RouteDetail;
import com.ecuatrails.api.dto.RouteListItem;
import com.ecuatrails.api.dto.RouteMap;
import com.ecuatrails.api.helpers.Mappers;
import com.ecuatrails.api.model.Route;
import com.ecuatrails.api.model.UserHistoryRoute;
import com.ecuatrails.api.repository.RouteRepository;
import com.ecuatrails.api.repository.UserHistoryRouteRepository;
import com.ecuatrails.api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RouteService {

	private final RouteRepository routeRepository;
	  private final UserRepository userRepository;
	  private final UserHistoryRouteRepository historyRepository;

	  public RouteService(RouteRepository routeRepository, UserRepository userRepository, UserHistoryRouteRepository historyRepository) {
	    this.routeRepository = routeRepository;
	    this.userRepository = userRepository;
	    this.historyRepository = historyRepository;
	  }

	  public Page<RouteListItem> list(Integer categoryId, String difficulty, String q, int page, int size) {
	    var pageable = PageRequest.of(page, size);
	    return routeRepository.search(categoryId, difficulty, q, pageable).map(Mappers::toRouteListItem);
	  }

	  public RouteDetail get(Integer id) {
	    Route r = routeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Route not found"));
	    r.getInterestPoints().size();
	    r.getLodgings().size();
	    return Mappers.toRouteDetail(r);
	  }

	  public RouteMap getMap(Integer id) {
	    Route r = routeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Route not found"));
	    r.getInterestPoints().size();
	    r.getLodgings().size();
	    return Mappers.toMap(r);
	  }

	  public void start(Integer routeId, Integer userId) {
	    var user = userRepository.findById(userId).orElseThrow();
	    var route = routeRepository.findById(routeId).orElseThrow();
	    historyRepository.findActives(userId, routeId).forEach(h -> { h.setIsFinished(true); });
	    var h = new UserHistoryRoute();
	    h.setUser(user); h.setRoute(route);
	    h.setRouteDate(LocalDateTime.now());
	    h.setIsFinished(false);
	    historyRepository.save(h);
	  }

	  public void finish(Integer routeId, Integer userId) {
	    var actives = historyRepository.findActives(userId, routeId);
	    if (actives.isEmpty()) {
	      start(routeId, userId);
	      historyRepository.findActives(userId, routeId).forEach(h -> h.setIsFinished(true));
	    } else {
	      actives.get(0).setIsFinished(true);
	    }
	  }
}
