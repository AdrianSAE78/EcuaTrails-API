package com.ecuatrails.api.dto;

import java.time.LocalDate;

public record MeDto(
	Integer id, String name, String lastName, String username, String email,
	LocalDate birthday, String authProvider, String uid
) {}
