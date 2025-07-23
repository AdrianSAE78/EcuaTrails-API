package com.ecuatrails.api.model;

import java.time.Duration;
import java.time.LocalDateTime;

import com.ecuatrails.api.helpers.converter.DurationConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Route {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer routeId;
	
	@Column(length = 64)
	private String name;
	
	@Column(length = 254)
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category; 
	
	@Convert(converter = DurationConverter.class)
	private Duration estimatedDuration;
	
	@Column(length = 64)
	private String recommendedSchedule;
	
	private Float distance;
	
	@Column(length = 16)
	private String difficulty;
	
	private Boolean status = true;
	
	private LocalDateTime created;
	
	private LocalDateTime updated;
	
	// Constructor
	public Route(Integer routeId, String name, String description, Category category, Duration estimatedDuration,
			String recommendedSchedule, Float distance, String difficulty, Boolean status, LocalDateTime created,
			LocalDateTime updated) {
		super();
		this.routeId = routeId;
		this.name = name;
		this.description = description;
		this.category = category;
		this.estimatedDuration = estimatedDuration;
		this.recommendedSchedule = recommendedSchedule;
		this.distance = distance;
		this.difficulty = difficulty;
		this.status = status;
		this.created = created;
		this.updated = updated;
	}
	
	// Empty Constructor
	public Route() {
		super();
	}

	// Getters and Setters
	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Duration getEstimatedDuration() {
		return estimatedDuration;
	}

	public void setEstimatedDuration(Duration estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}

	public String getRecommendedSchedule() {
		return recommendedSchedule;
	}

	public void setRecommendedSchedule(String recommendedSchedule) {
		this.recommendedSchedule = recommendedSchedule;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
}
