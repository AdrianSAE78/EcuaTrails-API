package com.ecuatrails.api.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.AddFavoriteRequest;
import com.ecuatrails.api.dto.FavoriteItemDto;
import com.ecuatrails.api.model.UserFavoriteRoute;
import com.ecuatrails.api.repository.RouteRepository;
import com.ecuatrails.api.repository.UserFavoriteRouteRepository;
import com.ecuatrails.api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FavoriteService {

	private final UserRepository userRepository;
	private final RouteRepository routeRepository;
	private final UserFavoriteRouteRepository favRepository;

	public FavoriteService(UserRepository userRepository, RouteRepository routeRepository, UserFavoriteRouteRepository favRepository) {
		this.userRepository = userRepository;
		this.routeRepository = routeRepository;
		this.favRepository = favRepository;
	}

	public Page<FavoriteItemDto> list(Integer userId, int page, int size) {
		var pageable = PageRequest.of(page, size);
		return favRepository.findByUser(userId, pageable).map(f ->
		new FavoriteItemDto(
				f.getUserFavoriteRouteId(),
				f.getRoute().getRouteId(),
				f.getRoute().getName(),
				f.getRoute().getDifficulty(),
				f.getRoute().getEstimatedDuration(),
				f.getCreated()
				)
				);
	}

	public FavoriteItemDto add(Integer userId, AddFavoriteRequest req) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
		var route = routeRepository.findById(req.routeId()).orElseThrow(() -> new NoSuchElementException("Route not found"));

		var existing = favRepository.findByUserAndRoute(userId, req.routeId());
		if (existing.isPresent()) {
			var f = existing.get();
			return new FavoriteItemDto(
					f.getUserFavoriteRouteId(), route.getRouteId(), route.getName(),
					route.getDifficulty(), route.getEstimatedDuration(), f.getCreated()
					);
		}

		var f = new UserFavoriteRoute();
		f.setUser(user);
		f.setRoute(route);
		var saved = favRepository.save(f);

		return new FavoriteItemDto(
				saved.getUserFavoriteRouteId(), route.getRouteId(), route.getName(),
				route.getDifficulty(), route.getEstimatedDuration(), saved.getCreated()
				);
	}

	public void delete(Integer userId, Integer favoriteId) {
		var f = favRepository.findOneForUser(userId, favoriteId)
				.orElseThrow(() -> new NoSuchElementException("Favorite not found"));
		favRepository.delete(f);
	}
}
