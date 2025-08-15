package com.ecuatrails.api.dto;

public record RouteTransport(
	Integer routeId,
	java.util.List<InterestPointTransportDto> items
) {}
