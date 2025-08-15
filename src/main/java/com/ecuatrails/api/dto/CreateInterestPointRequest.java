package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record CreateInterestPointRequest(String name, String description, String address, String city,
		String openingHours, BigDecimal rating, Integer reviewCount, Float latitude, Float longitude, Boolean status) {
}