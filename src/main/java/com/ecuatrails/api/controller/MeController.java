package com.ecuatrails.api.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.MeDto;
import com.ecuatrails.api.dto.MeStatsDto;
import com.ecuatrails.api.dto.Preference;
import com.ecuatrails.api.dto.UpdateMeRequest;
import com.ecuatrails.api.dto.UpdatePreferenceRequest;
import com.ecuatrails.api.service.ProfileService;

@RestController
@RequestMapping("/api/users/me")
public class MeController {

	private final ProfileService profileService;

	public MeController(ProfileService profileService) {
		this.profileService = profileService;
	}

	// GET /users/me
	@GetMapping
	public ResponseEntity<MeDto> getMe(Principal principal) {
		return ResponseEntity.ok(profileService.getMe(principal.getName()));
	}

	// PUT /users/me
	@PutMapping
	public ResponseEntity<MeDto> updateMe(@RequestBody UpdateMeRequest body, Principal principal) {
		return ResponseEntity.ok(profileService.updateMe(principal.getName(), body));
	}

	// GET /users/me/preferences
	@GetMapping("/preferences")
	public ResponseEntity<Preference> getPreferences(Principal principal) {
		return ResponseEntity.ok(profileService.getPreferences(principal.getName()));
	}

	// PUT /users/me/preferences
	@PutMapping("/preferences")
	public ResponseEntity<Preference> updatePreferences(@RequestBody UpdatePreferenceRequest body, Principal principal) {
		return ResponseEntity.ok(profileService.updatePreferences(principal.getName(), body));
	}

	// GET /users/me/stats
	@GetMapping("/stats")
	public ResponseEntity<MeStatsDto> getStats(Principal principal) {
		return ResponseEntity.ok(profileService.getStats(principal.getName()));
	}
}
