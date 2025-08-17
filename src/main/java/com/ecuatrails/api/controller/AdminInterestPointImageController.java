package com.ecuatrails.api.controller;

import java.util.List;

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

import com.ecuatrails.api.dto.ImageDto;
import com.ecuatrails.api.dto.ReorderImagesRequest;
import com.ecuatrails.api.dto.UpdateImageMetaRequest;
import com.ecuatrails.api.dto.UploadImageResponse;
import com.ecuatrails.api.dto.UploadPoiImageForm;
import com.ecuatrails.api.helpers.StorageService;
import com.ecuatrails.api.model.InterestPointImage;
import com.ecuatrails.api.repository.InterestPointImageRepository;
import com.ecuatrails.api.repository.InterestPointRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearer-jwt")
@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/interest-points")
@Tag(name = "Admin • InterestPoint Images", description = "Gestión de imágenes para POIs (admin)")
public class AdminInterestPointImageController {

	private final InterestPointRepository ipRepository;
	private final InterestPointImageRepository imageRepo;
	private final StorageService storage;

	public AdminInterestPointImageController(InterestPointRepository ipRepository,
			InterestPointImageRepository imageRepo, StorageService storage) {
		this.ipRepository = ipRepository;
		this.imageRepo = imageRepo;
		this.storage = storage;
	}

	@Operation(summary = "Listar imágenes de un POI", description = "Devuelve las imágenes del punto de interés en orden (posición ascendente).", operationId = "adminListPoiImages")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ImageDto.class), examples = @ExampleObject(name = "lista", value = "[\n"
					+ "  {\"id\": 11, \"url\": \"https://cdn/poi/301/1.jpg\", \"title\": \"Fachada\", \"alt\": \"Fachada\", \"cover\": true,  \"position\": 0},\n"
					+ "  {\"id\": 12, \"url\": \"https://cdn/poi/301/2.jpg\", \"title\": \"Sendero\", \"alt\": \"Sendero\", \"cover\": false, \"position\": 1}\n"
					+ "]"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (ADMIN requerido)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@GetMapping("/{id}/images")
	public List<ImageDto> list(@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id) {
		return imageRepo.listByPoi(id).stream().map(i -> new ImageDto(i.getInterestPointImageId(), i.getUrl(),
				i.getTitle(), i.getAlt(), i.getCover(), i.getPosition())).toList();
	}

	@Operation(summary = "Subir imagen", description = "Sube una imagen para el POI. Si `cover=true`, desmarca otras como portada.", operationId = "adminUploadPoiImage")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Subida", content = @Content(schema = @Schema(implementation = UploadImageResponse.class), examples = @ExampleObject(value = "{\n"
					+ "  \"image\": {\"id\": 13, \"url\": \"https://cdn/poi/301/new.jpg\", \"title\": \"Vista\", \"alt\": \"Vista\", \"cover\": false, \"position\": 2}\n"
					+ "}"))),
			@ApiResponse(responseCode = "400", description = "Archivo inválido o entrada inválida", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "403", description = "No autorizado (ADMIN requerido)", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI no encontrado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Formulario multipart con archivo y metadatos", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UploadPoiImageForm.class)))
	@PostMapping(path = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadImageResponse upload(@PathVariable Integer id,
			@RequestParam("file") org.springframework.web.multipart.MultipartFile file,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "alt", required = false) String alt,
			@RequestParam(value = "cover", required = false) Boolean cover) throws Exception {
		var poi = ipRepository.findById(id)
				.orElseThrow(() -> new java.util.NoSuchElementException("InterestPoint not found"));

		String ext = java.util.Optional.ofNullable(file.getOriginalFilename()).filter(f -> f.contains("."))
				.map(f -> f.substring(f.lastIndexOf('.') + 1)).orElse("jpg");

		String name = java.util.UUID.randomUUID() + "." + ext;
		String url = storage.upload("poi/" + id, name, file.getInputStream(), file.getContentType(), file.getSize());

		var img = new InterestPointImage();
		img.setInterestPoint(poi);
		img.setUrl(url);
		img.setTitle(title);
		img.setAlt(alt);
		img.setCover(cover != null ? cover : Boolean.FALSE);
		img.setPosition(imageRepo.listByPoi(id).size());
		var saved = imageRepo.save(img);

		if (Boolean.TRUE.equals(img.getCover())) {
			imageRepo.listByPoi(id).forEach(other -> {
				if (!other.getInterestPointImageId().equals(saved.getInterestPointImageId())
						&& Boolean.TRUE.equals(other.getCover())) {
					other.setCover(false);
				}
			});
		}
		var dto = new ImageDto(saved.getInterestPointImageId(), saved.getUrl(), saved.getTitle(), saved.getAlt(),
				saved.getCover(), saved.getPosition());
		return new UploadImageResponse(dto);
	}

	@Operation(summary = "Reordenar imágenes", description = "Actualiza la posición de las imágenes según `orderedIds` (0..n).", operationId = "adminReorderPoiImages")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ImageDto.class), examples = @ExampleObject(name = "lista", value = "[\n"
					+ "  {\"id\": 12, \"url\": \"https://cdn/poi/301/2.jpg\", \"title\": \"Sendero\", \"alt\": \"Sendero\", \"cover\": false, \"position\": 0},\n"
					+ "  {\"id\": 11, \"url\": \"https://cdn/poi/301/1.jpg\", \"title\": \"Fachada\", \"alt\": \"Fachada\", \"cover\": true,  \"position\": 1}\n"
					+ "]"))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "POI o imagen no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}/images/reorder")
	public List<ImageDto> reorder(@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "IDs en el nuevo orden", content = @Content(schema = @Schema(implementation = ReorderImagesRequest.class), examples = @ExampleObject(value = "{ \"orderedIds\": [12, 11, 13] }"))) @Valid @RequestBody ReorderImagesRequest body) {
		if (body == null || body.orderedIds() == null)
			return list(id);
		int pos = 0;
		var all = imageRepo.listByPoi(id);
		var map = all.stream()
				.collect(java.util.stream.Collectors.toMap(InterestPointImage::getInterestPointImageId, i -> i));
		for (Integer imageId : body.orderedIds()) {
			var img = map.get(imageId);
			if (img != null)
				img.setPosition(pos++);
		}
		return list(id);
	}

	@Operation(summary = "Actualizar metadatos de imagen", description = "Edita `title`, `alt` y `cover`. Si `cover=true`, desmarca otras portadas.", operationId = "adminUpdatePoiImageMeta")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Actualizada", content = @Content(schema = @Schema(implementation = ImageDto.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@PutMapping("/{id}/images/{imageId}")
	public ImageDto updateMeta(@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
			@Parameter(description = "ID de la imagen", example = "12") @PathVariable Integer imageId,
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
				imageRepo.listByPoi(id).forEach(other -> {
					if (!other.getInterestPointImageId().equals(imageId) && Boolean.TRUE.equals(other.getCover())) {
						other.setCover(false);
					}
				});
			}
		}
		return new ImageDto(img.getInterestPointImageId(), img.getUrl(), img.getTitle(), img.getAlt(), img.getCover(),
				img.getPosition());
	}

	@Operation(summary = "Eliminar imagen", description = "Elimina la imagen del POI.", operationId = "adminDeletePoiImage")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Eliminada"),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content(schema = @Schema(implementation = com.ecuatrails.api.dto.ApiError.class))) })
	@DeleteMapping("/{id}/images/{imageId}")
	public ResponseEntity<Void> delete(@Parameter(description = "ID del POI", example = "301") @PathVariable Integer id,
			@Parameter(description = "ID de la imagen", example = "12") @PathVariable Integer imageId) {
		var img = imageRepo.findById(imageId)
				.orElseThrow(() -> new java.util.NoSuchElementException("Image not found"));
		imageRepo.delete(img);
		return ResponseEntity.noContent().build();
	}
}