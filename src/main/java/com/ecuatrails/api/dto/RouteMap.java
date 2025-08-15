package com.ecuatrails.api.dto;

import java.util.List;

public record RouteMap(
	Integer routeId,
	List<double[]> points,
	List<double[]> lodgings
) {}
