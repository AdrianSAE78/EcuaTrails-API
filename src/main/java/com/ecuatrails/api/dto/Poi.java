package com.ecuatrails.api.dto;

public record Poi(
		Integer id, String name, String description, Float latitude, Float longitude, String address
) {

}
