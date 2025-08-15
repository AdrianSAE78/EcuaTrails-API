package com.ecuatrails.api.dto;

import java.math.BigDecimal;
import java.time.Duration;

public record UpdatePreferenceRequest(
	Integer categoryId, BigDecimal preferedBudget, Duration preferedDuration
) {}
