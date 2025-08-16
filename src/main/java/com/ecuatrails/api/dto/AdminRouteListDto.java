package com.ecuatrails.api.dto;

public record AdminRouteListDto(Integer id, String name, String categoryName, String difficulty, Float distance,
		Boolean status) {
}
