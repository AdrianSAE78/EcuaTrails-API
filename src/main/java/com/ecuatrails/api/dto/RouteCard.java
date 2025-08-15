package com.ecuatrails.api.dto;

import java.time.Duration;

public record RouteCard(Integer id, String name, String description,
        String category, Duration estimatedDuration, Float distance, String difficulty) {

}
