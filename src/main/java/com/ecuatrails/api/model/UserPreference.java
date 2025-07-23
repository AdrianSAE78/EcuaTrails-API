package com.ecuatrails.api.model;

import java.math.BigDecimal;
import java.time.Duration;

import com.ecuatrails.api.helpers.converter.DurationConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserPreference {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userPreferenceId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prefered_category")
	private Category category;

    @Column(precision = 10, scale = 2)
    private BigDecimal preferedBudget;
    
    @Convert(converter = DurationConverter.class)
    private Duration preferedDuration;
    
    // Constructor
    public UserPreference(Integer userPreferenceId, Category category, BigDecimal preferedBudget,
    		Duration preferedDuration) {
		super();
		this.userPreferenceId = userPreferenceId;
		this.category = category;
		this.preferedBudget = preferedBudget;
		this.preferedDuration = preferedDuration;
	}
    
    // Empty Constructor
    public UserPreference() {
		super();
	}

	// Getters and Setters
	public Integer getUserPreferenceId() {
		return userPreferenceId;
	}

	public void setUserPreferenceId(Integer userPreferenceId) {
		this.userPreferenceId = userPreferenceId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public BigDecimal getPreferedBudget() {
		return preferedBudget;
	}

	public void setPreferedBudget(BigDecimal preferedBudget) {
		this.preferedBudget = preferedBudget;
	}

	public Duration getPreferedDuration() {
		return preferedDuration;
	}

	public void setPreferedDuration(Duration preferedDuration) {
		this.preferedDuration = preferedDuration;
	}
    
    
}
