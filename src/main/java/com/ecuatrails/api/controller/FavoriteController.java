package com.ecuatrails.api.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.AddFavoriteRequest;
import com.ecuatrails.api.dto.FavoriteItemDto;
import com.ecuatrails.api.service.FavoriteService;
import com.ecuatrails.api.service.UserService;

@RestController
@RequestMapping("/api")
public class FavoriteController {

	private final FavoriteService favoriteService;
	private final UserService userService;

	public FavoriteController(FavoriteService favoriteService, UserService userService) {
		this.favoriteService = favoriteService;
		this.userService = userService;
	}

	// GET /users/{id}/favorites?page=&size=
	@GetMapping("/users/{id}/favorites")
	public ResponseEntity<Page<FavoriteItemDto>> list(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			Principal principal
			) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id)) return ResponseEntity.status(403).build();
		return ResponseEntity.ok(favoriteService.list(id, page, size));
	}

	// POST /users/{id}/favorites  { "routeId": 123 }
	@PostMapping("/users/{id}/favorites")
	public ResponseEntity<FavoriteItemDto> add(
			@PathVariable Integer id,
			@RequestBody AddFavoriteRequest body,
			Principal principal
			) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id)) return ResponseEntity.status(403).build();
		return ResponseEntity.ok(favoriteService.add(id, body));
	}

	// DELETE /users/{id}/favorites/{favoriteId}
	@DeleteMapping("/users/{id}/favorites/{favoriteId}")
	public ResponseEntity<Void> delete(
			@PathVariable Integer id,
			@PathVariable Integer favoriteId,
			Principal principal
			) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id)) return ResponseEntity.status(403).build();
		favoriteService.delete(id, favoriteId);
		return ResponseEntity.noContent().build();
	}
}
