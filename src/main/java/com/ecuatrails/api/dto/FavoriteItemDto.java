package com.ecuatrails.api.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public record FavoriteItemDto(
	Integer favoriteId,
	Integer routeId,
	String routeName,
	String difficulty,
	Duration estimatedDuration,
	LocalDateTime created
) {}
