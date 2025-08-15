package com.ecuatrails.api.dto;

import java.time.Duration;
import java.util.List;

public record RouteDetail(
	Integer id, String name, String description, String category, Duration estimatedDuration,
	Float distance, String difficulty, String recommendedSchedule,
	List<Poi> interestPoints, List<LodgingDto> lodgings,
	java.util.List<ImageDto> images, String coverImage
) {}
