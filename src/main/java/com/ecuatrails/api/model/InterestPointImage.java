package com.ecuatrails.api.model;

import java.time.LocalDateTime;

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
@Table(name = "interest_point_image")
public class InterestPointImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interestPointImageId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_point_id", nullable = false)
	private InterestPoint interestPoint;
	@Column(nullable = false, length = 512)
	private String url;
	@Column(length = 256)
	private String title;
	@Column(length = 256)
	private String alt;
	@Column(nullable = false)
	private Integer position = 0;
	@Column(nullable = false)
	private Boolean cover = false;
	private java.time.LocalDateTime created = java.time.LocalDateTime.now();
	
	// Constructor
	public InterestPointImage(Integer interestPointImageId, InterestPoint interestPoint, String url, String title,
			String alt, Integer position, Boolean cover, LocalDateTime created) {
		super();
		this.interestPointImageId = interestPointImageId;
		this.interestPoint = interestPoint;
		this.url = url;
		this.title = title;
		this.alt = alt;
		this.position = position;
		this.cover = cover;
		this.created = created;
	}
	
	// Empty Constructor
	public InterestPointImage() {
		super();
	}
	
	// Getters and Setters
	public Integer getInterestPointImageId() {
		return interestPointImageId;
	}
	public void setInterestPointImageId(Integer interestPointImageId) {
		this.interestPointImageId = interestPointImageId;
	}
	public InterestPoint getInterestPoint() {
		return interestPoint;
	}
	public void setInterestPoint(InterestPoint interestPoint) {
		this.interestPoint = interestPoint;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Boolean getCover() {
		return cover;
	}
	public void setCover(Boolean cover) {
		this.cover = cover;
	}
	public java.time.LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(java.time.LocalDateTime created) {
		this.created = created;
	}
}
