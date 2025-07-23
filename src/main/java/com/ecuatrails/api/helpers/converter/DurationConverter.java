package com.ecuatrails.api.helpers.converter;

import java.time.Duration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DurationConverter implements AttributeConverter<Duration, Long> {
	
	// Convert Duration to Long (minutes) for storing in DB
	@Override
    public Long convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return null;
        }
        return duration.toMinutes();
    }
    
    // Convert Long (minutes) to Duration for read from DB
	@Override
    public Duration convertToEntityAttribute(Long minutes) {
        if (minutes == null) {
            return null;
        }
        return Duration.ofMinutes(minutes);
    }
}
