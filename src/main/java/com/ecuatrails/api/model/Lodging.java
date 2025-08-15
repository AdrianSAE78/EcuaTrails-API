package com.ecuatrails.api.model;

import java.math.BigDecimal;
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
public class Lodging {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lodgingId;
	
	@ManyToMany(mappedBy = "lodgings")
	private List<Route> routes = new ArrayList<>();
	
	@OneToMany(mappedBy = "lodging", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("position asc, lodgingImageId asc")
	private java.util.List<LodgingImage> images = new java.util.ArrayList<>();
	
	@Column(length = 64)
	private String name;
	
	@Column(length = 128)
	private String description;
	
	@Column(precision = 10, scale = 2)
    private BigDecimal approximatePrice;
	
	private Float latitude;
	
	private Float longitude;
	
	private Boolean status = true;

	// Constructor
	public Lodging(Integer lodgingId, List<Route> routes, List<LodgingImage> images, String name, String description,
			BigDecimal approximatePrice, Float latitude, Float longitude, Boolean status) {
		super();
		this.lodgingId = lodgingId;
		this.routes = routes;
		this.images = images;
		this.name = name;
		this.description = description;
		this.approximatePrice = approximatePrice;
		this.latitude = latitude;
		this.longitude = longitude;
		this.status = status;
	}
	
	// Empty Constructor
	public Lodging() {
		super();
	}

	// Getters and Setters
	public Integer getLodgingId() {
		return lodgingId;
	}

	public void setLodgingId(Integer lodgingId) {
		this.lodgingId = lodgingId;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public java.util.List<LodgingImage> getImages() {
		return images;
	}

	public void setImages(java.util.List<LodgingImage> images) {
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

	public BigDecimal getApproximatePrice() {
		return approximatePrice;
	}

	public void setApproximatePrice(BigDecimal approximatePrice) {
		this.approximatePrice = approximatePrice;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
