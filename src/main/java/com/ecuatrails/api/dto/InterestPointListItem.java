package com.ecuatrails.api.dto;

public record InterestPointListItem(
	Integer id, String name, String description, Float latitude, Float longitude
) {}
