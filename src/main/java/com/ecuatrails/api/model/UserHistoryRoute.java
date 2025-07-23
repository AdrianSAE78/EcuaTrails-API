package com.ecuatrails.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserHistoryRoute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userHistoryRouteId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
	private Route route;
	
	private LocalDateTime routeDate;
	
	private Boolean isFinished = false;

	// Constructor
	public UserHistoryRoute(Integer userHistoryRouteId, User user, Route route, LocalDateTime routeDate,
			Boolean isFinished) {
		super();
		this.userHistoryRouteId = userHistoryRouteId;
		this.user = user;
		this.route = route;
		this.routeDate = routeDate;
		this.isFinished = isFinished;
	}
	
	// Empty Constructor
	public UserHistoryRoute() {
		super();
	}

	// Getters and Setters
	public Integer getUserHistoryRouteId() {
		return userHistoryRouteId;
	}

	public void setUserHistoryRouteId(Integer userHistoryRouteId) {
		this.userHistoryRouteId = userHistoryRouteId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public LocalDateTime getRouteDate() {
		return routeDate;
	}

	public void setRouteDate(LocalDateTime routeDate) {
		this.routeDate = routeDate;
	}

	public Boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}
}
