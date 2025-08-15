package com.ecuatrails.api.dto;

import java.time.LocalDate;

public record UpdateMeRequest(
	String name, String lastName, String email, LocalDate birthday
) {}
