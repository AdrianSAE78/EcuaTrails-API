package com.ecuatrails.api.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.CreateHistoryRequest;
import com.ecuatrails.api.dto.HistoryItem;
import com.ecuatrails.api.model.UserHistoryRoute;
import com.ecuatrails.api.repository.RouteRepository;
import com.ecuatrails.api.repository.UserHistoryRouteRepository;
import com.ecuatrails.api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HistoryService {

	private final UserRepository userRepository;
	private final RouteRepository routeRepository;
	private final UserHistoryRouteRepository historyRepository;

	public HistoryService(UserRepository userRepository, RouteRepository routeRepository, UserHistoryRouteRepository historyRepository) {
		this.userRepository = userRepository;
		this.routeRepository = routeRepository;
		this.historyRepository = historyRepository;
	}

	public Page<HistoryItem> list(Integer userId, int page, int size) {
		var pageable = PageRequest.of(page, size);
		return historyRepository.findByUser(userId, pageable).map(h ->
		new HistoryItem(
				h.getUserHistoryRouteId(),
				h.getRoute().getRouteId(),
				h.getRoute().getName(),
				h.getRoute().getDifficulty(),
				h.getRoute().getEstimatedDuration(),
				h.getRouteDate(),
				h.getIsFinished()
				)
				);
	}

	public HistoryItem create(Integer userId, CreateHistoryRequest req) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
		var route = routeRepository.findById(req.routeId())
				.orElseThrow(() -> new NoSuchElementException("Route not found"));

		var h = new UserHistoryRoute();
		h.setUser(user);
		h.setRoute(route);
		h.setRouteDate(req.routeDate() != null ? req.routeDate() : LocalDateTime.now());
		h.setIsFinished(req.isFinished() != null ? req.isFinished() : false);
		var saved = historyRepository.save(h);

		return new HistoryItem(
				saved.getUserHistoryRouteId(),
				route.getRouteId(),
				route.getName(),
				route.getDifficulty(),
				route.getEstimatedDuration(),
				saved.getRouteDate(),
				saved.getIsFinished()
				);
	}

	public void delete(Integer userId, Integer historyId) {
		var h = historyRepository.findOneByUserAndId(userId, historyId)
				.orElseThrow(() -> new NoSuchElementException("History not found"));
		historyRepository.delete(h);
	}

	// POST /routes/{id}/repeat
	public HistoryItem repeatRoute(Integer userId, Integer routeId) {
		var user = userRepository.findById(userId).orElseThrow();
		var route = routeRepository.findById(routeId).orElseThrow();

		var h = new UserHistoryRoute();
		h.setUser(user);
		h.setRoute(route);
		h.setRouteDate(LocalDateTime.now());
		h.setIsFinished(false);
		var saved = historyRepository.save(h);

		return new HistoryItem(
				saved.getUserHistoryRouteId(),
				route.getRouteId(),
				route.getName(),
				route.getDifficulty(),
				route.getEstimatedDuration(),
				saved.getRouteDate(),
				saved.getIsFinished()
				);
	}
}
