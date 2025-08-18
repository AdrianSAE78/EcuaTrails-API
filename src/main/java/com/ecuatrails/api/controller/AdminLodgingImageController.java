package com.ecuatrails.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.ecuatrails.api.dto.ImageDto;
import com.ecuatrails.api.dto.ReorderImagesRequest;
import com.ecuatrails.api.dto.UpdateImageMetaRequest;
import com.ecuatrails.api.dto.UploadImageResponse;
import com.ecuatrails.api.dto.UploadImagesResponse;
import com.ecuatrails.api.dto.UploadLodgingImageForm;
import com.ecuatrails.api.helpers.StorageService;
import com.ecuatrails.api.model.LodgingImage;
import com.ecuatrails.api.repository.LodgingImageRepository;
import com.ecuatrails.api.repository.LodgingRepository;

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
@RequestMapping("/api/admin/lodgings")
@Tag(name = "Admin • Lodging Images", description = "Gestión de imágenes para alojamientos")
public class AdminLodgingImageController {

	private final LodgingRepository lodgingRepository;
	private final LodgingImageRepository imageRepo;
	private final StorageService storage;

	public AdminLodgingImageController(LodgingRepository lodgingRepository, LodgingImageRepository imageRepo,
			StorageService storage) {
		this.lodgingRepository = lodgingRepository;
		this.imageRepo = imageRepo;
		this.storage = storage;
	}

	@Operation(summary = "Listar imágenes de un alojamiento", description = "Devuelve las imágenes del alojamiento ordenadas por `position` ascendente.", operationId = "adminListLodgingImages")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ImageDto.class), examples = @ExampleObject(name = "lista", value = "[\n"
					+ "  {\"id\": 41, \"url\": \"https://cdn/lodgings/11/1.jpg\", \"title\": \"Fachada\", \"alt\": \"Fachada\", \"cover\": true,  \"position\": 0},\n"
					+ "  {\"id\": 42, \"url\": \"https://cdn/lodgings/11/2.jpg\", \"title\": \"Lobby\",   \"alt\": \"Lobby\",   \"cover\": false, \"position\": 1}\n"
					+ "]"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Alojamiento no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/{id}/images")
	public List<ImageDto> list(
			@Parameter(description = "ID del alojamiento", example = "11") @PathVariable Integer id) {
		return imageRepo.listByLodging(id).stream().map(i -> new ImageDto(i.getLodgingImageId(), i.getUrl(),
				i.getTitle(), i.getAlt(), i.getCover(), i.getPosition())).toList();
	}

	@Operation(summary = "Subir imagen", description = "Sube una imagen para el alojamiento. Si `cover=true`, desmarca otras portadas.", operationId = "adminUploadLodgingImage")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Subida", content = @Content(schema = @Schema(implementation = UploadImageResponse.class), examples = @ExampleObject(value = "{\n"
					+ "  \"image\": {\"id\": 43, \"url\": \"https://cdn/lodgings/11/new.jpg\", \"title\": \"Vista\", \"alt\": \"Vista\", \"cover\": false, \"position\": 2}\n"
					+ "}"))),
			@ApiResponse(responseCode = "400", description = "Archivo/entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Alojamiento no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Formulario multipart con archivo y metadatos", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UploadLodgingImageForm.class)))
	@PostMapping(path = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadImagesResponse upload(@PathVariable Integer id, @RequestParam("files") MultipartFile[] files,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "alt", required = false) String alt,
			@RequestParam(value = "cover", required = false) Boolean cover) throws Exception {
		var lodging = lodgingRepository.findById(id)
				.orElseThrow(() -> new java.util.NoSuchElementException("Lodging not found"));

		int basePos = imageRepo.listByLodging(id).size();
		java.util.List<ImageDto> created = new java.util.ArrayList<>();

		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String ext = java.util.Optional.ofNullable(file.getOriginalFilename()).filter(f -> f.contains("."))
					.map(f -> f.substring(f.lastIndexOf('.') + 1)).orElse("jpg");

			String name = java.util.UUID.randomUUID() + "." + ext;
			String url = storage.upload("lodgings/" + id, name, file.getInputStream(), file.getContentType(),
					file.getSize());

			var img = new LodgingImage();
			img.setLodging(lodging);
			img.setUrl(url);
			img.setTitle(title);
			img.setAlt(alt);
			img.setCover(Boolean.TRUE.equals(cover) && i == 0);
			img.setPosition(basePos + i);

			var saved = imageRepo.save(img);
			created.add(new ImageDto(saved.getLodgingImageId(), saved.getUrl(), saved.getTitle(), saved.getAlt(),
					saved.getCover(), saved.getPosition()));
		}

		if (Boolean.TRUE.equals(cover) && !created.isEmpty()) {
			var first = created.get(0);
			imageRepo.listByLodging(id).forEach(other -> {
				if (!other.getLodgingImageId().equals(first.id()) && Boolean.TRUE.equals(other.getCover())) {
					other.setCover(false);
				}
			});
		}

		return new UploadImagesResponse(created);
	}

	@Operation(summary = "Reordenar imágenes", description = "Actualiza `position` según `orderedIds` (0..n).", operationId = "adminReorderLodgingImages")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ImageDto.class), examples = @ExampleObject(name = "lista", value = "[\n"
					+ "  {\"id\": 42, \"url\": \"https://cdn/lodgings/11/2.jpg\", \"title\": \"Lobby\",   \"alt\": \"Lobby\",   \"cover\": false, \"position\": 0},\n"
					+ "  {\"id\": 41, \"url\": \"https://cdn/lodgings/11/1.jpg\", \"title\": \"Fachada\", \"alt\": \"Fachada\", \"cover\": true,  \"position\": 1}\n"
					+ "]"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Alojamiento o imagen no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}/images/reorder")
	public List<ImageDto> reorder(
			@Parameter(description = "ID del alojamiento", example = "11") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "IDs en el nuevo orden", content = @Content(schema = @Schema(implementation = ReorderImagesRequest.class), examples = @ExampleObject(value = "{ \"orderedIds\": [42, 41, 43] }"))) @Valid @RequestBody ReorderImagesRequest body) {
		if (body == null || body.orderedIds() == null)
			return list(id);
		int pos = 0;
		var all = imageRepo.listByLodging(id);
		var map = all.stream().collect(java.util.stream.Collectors.toMap(LodgingImage::getLodgingImageId, i -> i));
		for (Integer imageId : body.orderedIds()) {
			var img = map.get(imageId);
			if (img != null)
				img.setPosition(pos++);
		}
		return list(id);
	}

	@Operation(summary = "Actualizar metadatos de imagen", description = "Edita `title`, `alt` y `cover`. Si `cover=true`, desmarca otras portadas.", operationId = "adminUpdateLodgingImageMeta")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizada", content = @Content(schema = @Schema(implementation = ImageDto.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}/images/{imageId}")
	public ImageDto updateMeta(@Parameter(description = "ID del alojamiento", example = "11") @PathVariable Integer id,
			@Parameter(description = "ID de la imagen", example = "42") @PathVariable Integer imageId,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Metadatos a editar", content = @Content(schema = @Schema(implementation = UpdateImageMetaRequest.class), examples = @ExampleObject(value = "{ \"title\": \"Nueva portada\", \"cover\": true }"))) @Valid @RequestBody UpdateImageMetaRequest body) {
		var img = imageRepo.findById(imageId)
				.orElseThrow(() -> new java.util.NoSuchElementException("Image not found"));
		if (body.title() != null)
			img.setTitle(body.title());
		if (body.alt() != null)
			img.setAlt(body.alt());
		if (body.cover() != null) {
			img.setCover(body.cover());
			if (Boolean.TRUE.equals(body.cover())) {
				imageRepo.listByLodging(id).forEach(other -> {
					if (!other.getLodgingImageId().equals(imageId) && Boolean.TRUE.equals(other.getCover())) {
						other.setCover(false);
					}
				});
			}
		}
		return new ImageDto(img.getLodgingImageId(), img.getUrl(), img.getTitle(), img.getAlt(), img.getCover(),
				img.getPosition());
	}

	@Operation(summary = "Eliminar imagen", description = "Elimina la imagen del alojamiento.", operationId = "adminDeleteLodgingImage")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminada"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}/images/{imageId}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "ID del alojamiento", example = "11") @PathVariable Integer id,
			@Parameter(description = "ID de la imagen", example = "42") @PathVariable Integer imageId) {
		var img = imageRepo.findById(imageId)
				.orElseThrow(() -> new java.util.NoSuchElementException("Image not found"));
		imageRepo.delete(img);
		return ResponseEntity.noContent().build();
	}
}
