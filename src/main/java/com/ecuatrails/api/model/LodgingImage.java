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
@Table(name = "lodging_image")
public class LodgingImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lodgingImageId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lodging_id", nullable = false)
	private Lodging lodging;
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
	public LodgingImage(Integer lodgingImageId, Lodging lodging, String url, String title, String alt, Integer position,
			Boolean cover, LocalDateTime created) {
		super();
		this.lodgingImageId = lodgingImageId;
		this.lodging = lodging;
		this.url = url;
		this.title = title;
		this.alt = alt;
		this.position = position;
		this.cover = cover;
		this.created = created;
	}
	
	// Empty Constructor
	public LodgingImage() {
		super();
	}
	
	// Getters and Setters
	public Integer getLodgingImageId() {
		return lodgingImageId;
	}
	public void setLodgingImageId(Integer lodgingImageId) {
		this.lodgingImageId = lodgingImageId;
	}
	public Lodging getLodging() {
		return lodging;
	}
	public void setLodging(Lodging lodging) {
		this.lodging = lodging;
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
