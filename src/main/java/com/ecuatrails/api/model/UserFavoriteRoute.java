package com.ecuatrails.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_favorite_route",
uniqueConstraints = @UniqueConstraint(name = "uk_user_route", columnNames = {"user_id","route_id"}))
public class UserFavoriteRoute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userFavoriteRouteId;

	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "route_id", nullable = false)
	private Route route;

	private LocalDateTime created = LocalDateTime.now();
	
	// Constructor
	public UserFavoriteRoute(Integer userFavoriteRouteId, User user, Route route, LocalDateTime created) {
		super();
		this.userFavoriteRouteId = userFavoriteRouteId;
		this.user = user;
		this.route = route;
		this.created = created;
	}
	
	// Empty Constructor
	public UserFavoriteRoute() {
		super();
	}
		
	// Getters and Setters
	public Integer getUserFavoriteRouteId() { return userFavoriteRouteId; }
	public void setUserFavoriteRouteId(Integer id) { this.userFavoriteRouteId = id; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public Route getRoute() { return route; }
	public void setRoute(Route route) { this.route = route; }
	public LocalDateTime getCreated() { return created; }
	public void setCreated(LocalDateTime created) { this.created = created; }
}
