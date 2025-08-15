package com.ecuatrails.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.InterestPointDetail;
import com.ecuatrails.api.dto.InterestPointListItem;
import com.ecuatrails.api.service.PoiService;

@RestController
@RequestMapping("/api")
public class PoiController {
	
	private final PoiService poiService;

	  public PoiController(PoiService poiService) {
	    this.poiService = poiService;
	  }

	  // GET /routes/{id}/interest-points
	  @GetMapping("/routes/{id}/interest-points")
	  public ResponseEntity<List<InterestPointListItem>> list(@PathVariable Integer id) {
	    return ResponseEntity.ok(poiService.listByRoute(id));
	  }

	  // GET /interest-points/{id}
	  @GetMapping("/interest-points/{id}")
	  public ResponseEntity<InterestPointDetail> get(@PathVariable Integer id) {
	    return ResponseEntity.ok(poiService.get(id));
	  }
}
