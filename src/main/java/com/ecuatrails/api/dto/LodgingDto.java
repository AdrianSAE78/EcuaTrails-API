package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record LodgingDto(
	Integer id, String name, String description, BigDecimal approximatePrice, Float latitude, Float longitude
) {
	
}
