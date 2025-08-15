package com.ecuatrails.api.dto;

import java.math.BigDecimal;

public record AdminTransportList(Integer id, String name, String type, String route, String busLine,
		BigDecimal baseFare, Boolean accessibility, Boolean status) {
}
