package com.ecuatrails.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class InterestPoint {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interestPointId;
	
	@ManyToMany(mappedBy = "interestPoints")
	private List<Route> routes = new ArrayList<>();
	
	@OneToMany(mappedBy = "interestPoint", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("position asc, interestPointImageId asc")
	private java.util.List<InterestPointImage> images = new java.util.ArrayList<>();
	
	@Column(length = 128)
	private String name;
	
	@Column(length = 64)
	private String description;
	
	private Float latitude;
	
	private Float longitude;
	
	@Column(length = 300)
	private String address;
	
	@Column(length = 50)
	private String city;
	
	@Column(length = 500)
	private String openingHours;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal rating;
	
	private Integer reviewCount;
	
	private LocalDateTime Created;
	
	private LocalDateTime Modified;
	
	@Column(name = "status") 
	private Boolean Status = true;

	// Constructor
	public InterestPoint(Integer interestPointId, List<Route> routes, List<InterestPointImage> images, String name,
			String description, Float latitude, Float longitude, String address, String city, String openingHours,
			BigDecimal rating, Integer reviewCount, LocalDateTime created, LocalDateTime modified, Boolean status) {
		super();
		this.interestPointId = interestPointId;
		this.routes = routes;
		this.images = images;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.city = city;
		this.openingHours = openingHours;
		this.rating = rating;
		this.reviewCount = reviewCount;
		Created = created;
		Modified = modified;
		Status = status;
	}
	
	// Empty Constructor
	public InterestPoint() {
		super();
	}

	// Getters and Setters
	public Integer getInterestPointId() {
		return interestPointId;
	}

	public void setInterestPointId(Integer interestPointId) {
		this.interestPointId = interestPointId;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public java.util.List<InterestPointImage> getImages() {
		return images;
	}

	public void setImages(java.util.List<InterestPointImage> images) {
		this.images = images;
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

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public LocalDateTime getCreated() {
		return Created;
	}

	public void setCreated(LocalDateTime created) {
		Created = created;
	}

	public LocalDateTime getModified() {
		return Modified;
	}

	public void setModified(LocalDateTime modified) {
		Modified = modified;
	}

	public Boolean getStatus() {
		return Status;
	}

	public void setStatus(Boolean status) {
		Status = status;
	}
}
