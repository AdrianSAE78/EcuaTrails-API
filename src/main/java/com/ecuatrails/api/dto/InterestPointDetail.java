package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record InterestPointDetail(Integer id, String name, String description, Float latitude, Float longitude,
		String address, String city, String openingHours, BigDecimal rating, Integer reviewCount,
		java.util.List<ImageDto> images, String coverImage) {
}
