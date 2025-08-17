package com.ecuatrails.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.AdminIptDto;
import com.ecuatrails.api.dto.AdminTransportDetail;
import com.ecuatrails.api.dto.AdminTransportList;
import com.ecuatrails.api.dto.CreateIptLinkRequest;
import com.ecuatrails.api.dto.CreateTransportRequest;
import com.ecuatrails.api.dto.PageAdminTransportListResponse;
import com.ecuatrails.api.dto.UpdateTransportRequest;
import com.ecuatrails.api.service.AdminTransportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin • Transport", description = "Administración de medios de transporte y enlaces a POIs")
public class AdminTransportController {

  private final AdminTransportService service;

  public AdminTransportController(AdminTransportService service) {
    this.service = service;
  }

  // --- Transport ---

  @Operation(
      summary = "Listar transportes",
      description = "Lista paginada con filtros por texto, tipo y estado.",
      operationId = "adminListTransport"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK",
          content = @Content(schema = @Schema(implementation = PageAdminTransportListResponse.class),
              examples = @ExampleObject(name = "page", value =
                  "{\n" +
                  "  \"content\": [\n" +
                  "    {\"id\": 1, \"name\": \"Bus 24\", \"type\": \"BUS\", \"route\": \"Av. Central\", \"busLine\": \"24\", \"baseFare\": 0.35, \"accessibility\": true, \"status\": true}\n" +
                  "  ],\n" +
                  "  \"page\": 0,\n" +
                  "  \"size\": 10,\n" +
                  "  \"totalPages\": 2,\n" +
                  "  \"totalElements\": 12\n" +
                  "}"
              ))),
      @ApiResponse(responseCode = "401", description = "No autenticado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "403", description = "No autorizado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @GetMapping("/transport")
  public ResponseEntity<Page<AdminTransportList>> listTransport(
      @Parameter(description = "Número de página (0-based)", example = "0")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamaño de página", example = "10")
      @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Texto de búsqueda", example = "bus")
      @RequestParam(required = false) String q,
      @Parameter(description = "Tipo (p.ej. BUS, METRO, TAXI)", example = "BUS")
      @RequestParam(required = false) String type,
      @Parameter(description = "Estado (true=activo, false=inactivo)", example = "true")
      @RequestParam(required = false) Boolean status
  ) {
    return ResponseEntity.ok(service.list(q, type, status, page, size));
  }

  @Operation(
      summary = "Crear transporte",
      description = "Crea un nuevo medio de transporte.",
      operationId = "adminCreateTransport"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Creado",
          content = @Content(schema = @Schema(implementation = AdminTransportDetail.class),
              examples = @ExampleObject(value =
                  "{ \"id\": 2, \"name\": \"Metro A\", \"type\": \"METRO\", \"route\": \"Troncal A\", \"busLine\": null, \"schedule\": \"06:00-22:00\", \"baseFare\": 0.45, \"accessibility\": true, \"status\": true }"
              ))),
      @ApiResponse(responseCode = "400", description = "Entrada inválida",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "401", description = "No autenticado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @PostMapping("/transport")
  public ResponseEntity<AdminTransportDetail> createTransport(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true, description = "Nuevo transporte",
          content = @Content(schema = @Schema(implementation = CreateTransportRequest.class))
      )
      @Valid @RequestBody CreateTransportRequest body
  ) {
    return ResponseEntity.ok(service.create(body));
  }

  @Operation(
      summary = "Actualizar transporte",
      description = "Actualiza parcialmente un transporte por ID.",
      operationId = "adminUpdateTransport"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Actualizado",
          content = @Content(schema = @Schema(implementation = AdminTransportDetail.class))),
      @ApiResponse(responseCode = "400", description = "Entrada inválida",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "401", description = "No autenticado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "404", description = "Transporte no encontrado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @PutMapping("/transport/{id}")
  public ResponseEntity<AdminTransportDetail> updateTransport(
      @Parameter(description = "ID del transporte", example = "2") @PathVariable Integer id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true, description = "Campos a actualizar (parciales)",
          content = @Content(schema = @Schema(implementation = UpdateTransportRequest.class))
      )
      @Valid @RequestBody UpdateTransportRequest body
  ) {
    return ResponseEntity.ok(service.update(id, body));
  }

  @Operation(
      summary = "Eliminar transporte",
      description = "Elimina el transporte si no está vinculado a puntos de interés.",
      operationId = "adminDeleteTransport"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Eliminado"),
      @ApiResponse(responseCode = "401", description = "No autenticado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "404", description = "Transporte no encontrado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "409", description = "En uso por POIs",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @DeleteMapping("/transport/{id}")
  public ResponseEntity<Void> deleteTransport(
      @Parameter(description = "ID del transporte", example = "2") @PathVariable Integer id
  ) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  // --- Links InterestPoint ↔ Transport ---

  @Operation(
      summary = "Listar transportes por POI",
      description = "Devuelve los enlaces transporte-POI para un punto de interés.",
      operationId = "adminListTransportByPoi"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK",
          content = @Content(schema = @Schema(implementation = AdminIptDto.class),
              examples = @ExampleObject(name = "lista", value =
                  "[{\"id\": 101, \"interestPointId\": 301, \"interestPointName\": \"Mirador\", \"transportId\": 2, \"transportName\": \"Metro A\", \"walkingDistanceMeters\": 350, \"estimatedWalkingTime\": 5, \"accessibilityNotes\": \"Rampa disponible\", \"status\": true}]"
              ))),
      @ApiResponse(responseCode = "404", description = "POI no encontrado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @GetMapping("/interest-points/{id}/transport")
  public ResponseEntity<List<AdminIptDto>> listByPoi(
      @Parameter(description = "ID del POI", example = "301") @PathVariable Integer id
  ) {
    return ResponseEntity.ok(service.listByPoi(id));
  }

  @Operation(
      summary = "Vincular transporte a POI",
      description = "Crea el enlace transporte-POI con distancia/tiempo a pie y notas de accesibilidad.",
      operationId = "adminLinkTransportToPoi"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Vinculado",
          content = @Content(schema = @Schema(implementation = AdminIptDto.class),
              examples = @ExampleObject(value =
                  "{ \"id\": 102, \"interestPointId\": 301, \"interestPointName\": \"Mirador\", \"transportId\": 1, \"transportName\": \"Bus 24\", \"walkingDistanceMeters\": 300, \"estimatedWalkingTime\": 4, \"accessibilityNotes\": \"Cruce peatonal\", \"status\": true }"
              ))),
      @ApiResponse(responseCode = "400", description = "Entrada inválida",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
      @ApiResponse(responseCode = "404", description = "POI o transporte no encontrado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @PostMapping("/interest-points/{id}/transport")
  public ResponseEntity<AdminIptDto> link(
      @Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true, description = "Datos del enlace",
          content = @Content(schema = @Schema(implementation = CreateIptLinkRequest.class))
      )
      @Valid @RequestBody CreateIptLinkRequest body
  ) {
    return ResponseEntity.ok(service.link(id, body));
  }

  @Operation(
      summary = "Eliminar enlace transporte-POI",
      description = "Elimina un vínculo específico entre un transporte y un POI.",
      operationId = "adminUnlinkTransportFromPoi"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Eliminado"),
      @ApiResponse(responseCode = "404", description = "Vínculo o POI no encontrado",
          content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class)))
  })
  @DeleteMapping("/interest-points/{id}/transport/{interestPointTransportId}")
  public ResponseEntity<Void> unlink(
      @Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
      @Parameter(description = "ID del vínculo", example = "101") @PathVariable Integer interestPointTransportId
  ) {
    service.unlink(id, interestPointTransportId);
    return ResponseEntity.noContent().build();
  }
}
