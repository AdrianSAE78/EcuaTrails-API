package com.ecuatrails.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InterestPointTransport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interestPointTransportId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_point_id")
	private InterestPoint interestPoint;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
	private Transport transport;
	
	private Integer walkingDistanceMeters;
	
	private Integer estimatedWalkingTime;
	
	@Column(length = 254)
    private String accessibilityNotes;
	
	private Boolean status;
	
	// Constructor
	public InterestPointTransport(Integer interestPointTransportId, InterestPoint interestPoint, Transport transport,
			Integer walkingDistanceMeters, Integer estimatedWalkingTime, String accessibilityNotes, Boolean status) {
		super();
		this.interestPointTransportId = interestPointTransportId;
		this.interestPoint = interestPoint;
		this.transport = transport;
		this.walkingDistanceMeters = walkingDistanceMeters;
		this.estimatedWalkingTime = estimatedWalkingTime;
		this.accessibilityNotes = accessibilityNotes;
		this.status = status;
	}
	
	// Empty Constructor
	public InterestPointTransport() {
		super();
	}

	// Getters and Setters
	public Integer getInterestPointTransportId() {
		return interestPointTransportId;
	}

	public void setInterestPointTransportId(Integer interestPointTransportId) {
		this.interestPointTransportId = interestPointTransportId;
	}

	public InterestPoint getInterestPoint() {
		return interestPoint;
	}

	public void setInterestPoint(InterestPoint interestPoint) {
		this.interestPoint = interestPoint;
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

	public Integer getWalkingDistanceMeters() {
		return walkingDistanceMeters;
	}

	public void setWalkingDistanceMeters(Integer walkingDistanceMeters) {
		this.walkingDistanceMeters = walkingDistanceMeters;
	}

	public Integer getEstimatedWalkingTime() {
		return estimatedWalkingTime;
	}

	public void setEstimatedWalkingTime(Integer estimatedWalkingTime) {
		this.estimatedWalkingTime = estimatedWalkingTime;
	}

	public String getAccessibilityNotes() {
		return accessibilityNotes;
	}

	public void setAccessibilityNotes(String accessibilityNotes) {
		this.accessibilityNotes = accessibilityNotes;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
