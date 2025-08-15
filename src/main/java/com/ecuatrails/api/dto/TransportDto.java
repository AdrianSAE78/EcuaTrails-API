package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record TransportDto(
	Integer id, String name, String type, String route, String busLine,
	String schedule, BigDecimal baseFare, Boolean accessibility, Boolean status
) {}