package com.ecuatrails.api.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.MeDto;
import com.ecuatrails.api.dto.MeStatsDto;
import com.ecuatrails.api.dto.Preference;
import com.ecuatrails.api.dto.UpdateMeRequest;
import com.ecuatrails.api.dto.UpdatePreferenceRequest;
import com.ecuatrails.api.repository.CategoryRepository;
import com.ecuatrails.api.repository.UserHistoryRouteRepository;
import com.ecuatrails.api.repository.UserRepository;
import com.ecuatrails.api.model.User;
import com.ecuatrails.api.model.UserPreference;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfileService {

	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final UserHistoryRouteRepository historyRepository;

	public ProfileService(UserRepository userRepository,
			CategoryRepository categoryRepository,
			UserHistoryRouteRepository historyRepository) {
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.historyRepository = historyRepository;
	}

	public MeDto getMe(String username) {
		User u = userRepository.findByUsername(username)
				 .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));;
		return new MeDto(
				u.getUserId(), u.getName(), u.getLastName(), u.getUsername(), u.getEmail(),
				u.getBirthday(), u.getAuthProvider(), u.getUid()
				);
	}

	public MeDto updateMe(String username, UpdateMeRequest req) {
		User u = userRepository.findByUsername(username)
				 .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));;
		if (req.name() != null) u.setName(req.name());
		if (req.lastName() != null) u.setLastName(req.lastName());
		if (req.email() != null) u.setEmail(req.email());
		if (req.birthday() != null) u.setBirthday(req.birthday());
		return new MeDto(
				u.getUserId(), u.getName(), u.getLastName(), u.getUsername(), u.getEmail(),
				u.getBirthday(), u.getAuthProvider(), u.getUid()
				);
	}

	public Preference getPreferences(String username) {
		User u = userRepository.findByUsername(username)
				 .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));;
		UserPreference p = u.getUserPreference();
		Integer categoryId = p != null && p.getCategory() != null ? p.getCategory().getCategoryId() : null;
		return new Preference(
				categoryId,
				p != null ? p.getPreferedBudget() : null,
						p != null ? p.getPreferedDuration() : null
				);
	}

	public Preference updatePreferences(String username, UpdatePreferenceRequest req) {
		User u = userRepository.findByUsername(username)
				 .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));;
		UserPreference p = u.getUserPreference();
		if (p == null) { p = new UserPreference(); u.setUserPreference(p); }

		if (req.categoryId() != null) {
			var cat = categoryRepository.findById(req.categoryId())
					.orElseThrow(() -> new NoSuchElementException("Category not found"));
			p.setCategory(cat);
		}
		if (req.preferedBudget() != null) p.setPreferedBudget(req.preferedBudget());
		if (req.preferedDuration() != null) p.setPreferedDuration(req.preferedDuration());

		Integer categoryId = p.getCategory() != null ? p.getCategory().getCategoryId() : null;
		return new Preference(categoryId, p.getPreferedBudget(), p.getPreferedDuration());
	}

	public MeStatsDto getStats(String username) {
		User u = userRepository.findByUsername(username)
				 .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));;
		long finished = historyRepository.countFinished(u.getUserId());
		float totalKm = (historyRepository.sumDistanceFinished(u.getUserId()) == null ? 0f : historyRepository.sumDistanceFinished(u.getUserId())) / 1_000f;
		var dur = historyRepository.sumDurationFinished(u.getUserId());
		long minutes = dur == null ? 0 : dur.toMinutes();
		var last = historyRepository.findRecent(u.getUserId(), PageRequest.of(0, 1));
		java.time.LocalDateTime lastAt = last.isEmpty() ? null : last.get(0).getRouteDate();

		return new MeStatsDto(finished, totalKm, minutes, lastAt);
	}
}
