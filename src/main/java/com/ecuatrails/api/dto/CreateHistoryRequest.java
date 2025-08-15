package com.ecuatrails.api.dto;

import java.time.LocalDateTime;

public record CreateHistoryRequest(Integer routeId, Boolean isFinished, LocalDateTime routeDate) {}
