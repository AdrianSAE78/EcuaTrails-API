package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record UpdateLodgingRequest(String name, String description, BigDecimal approximatePrice, Float latitude,
		Float longitude, Boolean status) {
}
