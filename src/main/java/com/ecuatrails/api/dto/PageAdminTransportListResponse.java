package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "PageAdminTransportListResponse", description = "Página de transportes (admin)")
public record PageAdminTransportListResponse(
		@Schema(description = "Elementos de la página") List<AdminTransportList> content,
		@Schema(example = "0") int page, @Schema(example = "10") int size, @Schema(example = "2") int totalPages,
		@Schema(example = "12") long totalElements) {
}
