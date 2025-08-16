package com.ecuatrails.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_lodging")
public class RouteLodging {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer routeLodgingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id", nullable = false)
	private Route route;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lodging_id", nullable = false)
	private Lodging lodging;

	// Constructor
	public RouteLodging(Integer routeLodgingId, Route route, Lodging lodging) {
		super();
		this.routeLodgingId = routeLodgingId;
		this.route = route;
		this.lodging = lodging;
	}

	// Empty Constructor
	public RouteLodging() {
		super();
	}

	// Getters and Setters
	public Integer getRouteLodgingId() {
		return routeLodgingId;
	}

	public void setRouteLodgingId(Integer routeLodgingId) {
		this.routeLodgingId = routeLodgingId;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Lodging getLodging() {
		return lodging;
	}

	public void setLodging(Lodging lodging) {
		this.lodging = lodging;
	}
}
