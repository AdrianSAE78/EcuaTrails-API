package com.ecuatrails.api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.RouteCard;
import com.ecuatrails.api.helpers.converter.Mappers;
import com.ecuatrails.api.model.User;
import com.ecuatrails.api.model.UserPreference;
import com.ecuatrails.api.repository.RouteRepository;
import com.ecuatrails.api.repository.UserHistoryRouteRepository;
import com.ecuatrails.api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecommendationService {
	private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final UserHistoryRouteRepository historyRepository;

    public RecommendationService(RouteRepository routeRepository, UserRepository userRepository, UserHistoryRouteRepository historyRepository) {
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    public List<RouteCard> recommendedForUser(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));

        // UserPreferences
        UserPreference pref = user.getUserPreference();
        Integer categoryId = (pref != null && pref.getCategory() != null)
                ? pref.getCategory().getCategoryId() : null;

        Duration maxDuration = (pref != null) ? pref.getPreferedDuration() : null;

        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        List<Integer> exclude = historyRepository.findFinishedRouteIdsSince(userId, cutoff);

        return routeRepository.findRecommended(categoryId, maxDuration,
                    exclude.isEmpty() ? null : exclude)
                .stream()
                .map(Mappers::toCard)
                .collect(Collectors.toList());
    }
}
