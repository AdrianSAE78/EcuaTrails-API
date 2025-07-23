package com.ecuatrails.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transportId;

	@Column(length = 100)
	private String name;
	
	@Column(length = 16)
	private String type;
	
	@Column(length = 100)
	private String route;
	
	@Column(length = 64)
	private String busLine;
	
	@Column(length = 500)
	private String schedule;
	
	@Column(precision = 8, scale = 2)
    private BigDecimal baseFare;
	
	private Boolean accessibility;
	
	private LocalDateTime created;
	
	private LocalDateTime modified;
	
	private Boolean status;
	
	// Constructor
	public Transport(Integer transportId, String name, String type, String route, String busLine, String schedule,
			BigDecimal baseFare, Boolean accessibility, LocalDateTime created, LocalDateTime modified, Boolean status) {
		super();
		this.transportId = transportId;
		this.name = name;
		this.type = type;
		this.route = route;
		this.busLine = busLine;
		this.schedule = schedule;
		this.baseFare = baseFare;
		this.accessibility = accessibility;
		this.created = created;
		this.modified = modified;
		this.status = status;
	}
	
	// Empty Constructor
	public Transport() {
		super();
	}
	
	// Getters and Setters
	public Integer getTransportId() {
		return transportId;
	}

	public void setTransportId(Integer transportId) {
		this.transportId = transportId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getBusLine() {
		return busLine;
	}

	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public BigDecimal getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(BigDecimal baseFare) {
		this.baseFare = baseFare;
	}

	public Boolean getAccessibility() {
		return accessibility;
	}

	public void setAccessibility(Boolean accessibility) {
		this.accessibility = accessibility;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
