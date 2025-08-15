package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record LodgingDetail(Integer id, String name, String description, BigDecimal approximatePrice, Float latitude,
		Float longitude, java.util.List<ImageDto> images, String coverImage) {
}