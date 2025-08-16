package com.ecuatrails.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_interest_point")
public class RouteInterestPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer routeInterestPointId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id", nullable = false)
	private Route route;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_point_id", nullable = false)
	private InterestPoint interestPoint;

	@Column(name = "position", nullable = false)
	private Integer position = 0;

	// Constructor
	public RouteInterestPoint(Integer routeInterestPointId, Route route, InterestPoint interestPoint,
			Integer position) {
		super();
		this.routeInterestPointId = routeInterestPointId;
		this.route = route;
		this.interestPoint = interestPoint;
		this.position = position;
	}

	// Empty Constructor
	public RouteInterestPoint() {
		super();
	}

	// Getters and Setters
	public Integer getRouteInterestPointId() {
		return routeInterestPointId;
	}

	public void setRouteInterestPointId(Integer routeInterestPointId) {
		this.routeInterestPointId = routeInterestPointId;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public InterestPoint getInterestPoint() {
		return interestPoint;
	}

	public void setInterestPoint(InterestPoint interestPoint) {
		this.interestPoint = interestPoint;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
