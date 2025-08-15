package com.ecuatrails.api.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public record HistoryItem(
	Integer historyId,
	Integer routeId,
	String routeName,
	String difficulty,
	Duration estimatedDuration,
	LocalDateTime routeDate,
	Boolean isFinished
) {}
