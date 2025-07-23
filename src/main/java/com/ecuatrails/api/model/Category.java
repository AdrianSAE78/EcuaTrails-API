package com.ecuatrails.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

	@Column(length = 16)
    private String code;
	
    @Column(length = 64)
    private String name;

    // Constructor
    public Category(Integer categoryId, String code, String name) {
		super();
		this.categoryId = categoryId;
		this.code = code;
		this.name = name;
	}
    
    // Empty Constructor
    public Category() {
		super();
	}

	// Getters and Setters
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
}
