package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BulkInterestPointRequest", description = "Request para creación masiva")
public record BulkInterestPointRequest(
		@Schema(description = "Elementos a crear") List<CreateInterestPointRequest> items) {
}
