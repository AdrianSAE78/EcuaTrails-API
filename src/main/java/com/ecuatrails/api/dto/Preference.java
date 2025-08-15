package com.ecuatrails.api.dto;

import java.math.BigDecimal;
import java.time.Duration;

public record Preference(
	Integer categoryId, BigDecimal preferedBudget, Duration preferedDuration
) {}
