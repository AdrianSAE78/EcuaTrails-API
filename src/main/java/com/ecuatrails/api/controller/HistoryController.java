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

import com.ecuatrails.api.dto.CreateHistoryRequest;
import com.ecuatrails.api.dto.HistoryItem;
import com.ecuatrails.api.service.HistoryService;
import com.ecuatrails.api.service.UserService;

@RestController
@RequestMapping("/api")
public class HistoryController {

	private final HistoryService historyService;
	private final UserService userService;

	public HistoryController(HistoryService historyService, UserService userService) {
		this.historyService = historyService;
		this.userService = userService;
	}

	// GET /users/{id}/history?page=&size=
	@GetMapping("/users/{id}/history")
	public ResponseEntity<Page<HistoryItem>> getHistory(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			Principal principal
			) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id)) return ResponseEntity.status(403).build();
		return ResponseEntity.ok(historyService.list(id, page, size));
	}

	// POST /users/{id}/history
	@PostMapping("/users/{id}/history")
	public ResponseEntity<HistoryItem> addHistory(
			@PathVariable Integer id,
			@RequestBody CreateHistoryRequest body,
			Principal principal
			) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id)) return ResponseEntity.status(403).build();
		return ResponseEntity.ok(historyService.create(id, body));
	}

	// DELETE /users/{id}/history/{historyId}
	@DeleteMapping("/users/{id}/history/{historyId}")
	public ResponseEntity<Void> deleteHistory(
			@PathVariable Integer id,
			@PathVariable Integer historyId,
			Principal principal
			) {
		var me = userService.findByUsername(principal.getName());
		if (!me.getUserId().equals(id)) return ResponseEntity.status(403).build();
		historyService.delete(id, historyId);
		return ResponseEntity.noContent().build();
	}

	// POST /routes/{id}/repeat
	@PostMapping("/routes/{id}/repeat")
	public ResponseEntity<HistoryItem> repeat(@PathVariable Integer id, Principal principal) {
		var me = userService.findByUsername(principal.getName());
		return ResponseEntity.ok(historyService.repeatRoute(me.getUserId(), id));
	}
}
