package com.ecuatrails.api.controller;

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
import com.ecuatrails.api.helpers.converter.StorageService;
import com.ecuatrails.api.model.LodgingImage;
import com.ecuatrails.api.repository.LodgingImageRepository;
import com.ecuatrails.api.repository.LodgingRepository;

@RestController
@RequestMapping("/api/admin/lodging")
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

	@GetMapping("/{id}/images")
	public java.util.List<ImageDto> list(@PathVariable Integer id) {
		return imageRepo.listByLodging(id).stream().map(i -> new ImageDto(i.getLodgingImageId(), i.getUrl(),
				i.getTitle(), i.getAlt(), i.getCover(), i.getPosition())).toList();
	}

	@PostMapping(path = "/{id}/images", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadImageResponse upload(@PathVariable Integer id,
			@RequestParam("file") org.springframework.web.multipart.MultipartFile file,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "alt", required = false) String alt,
			@RequestParam(value = "cover", required = false) Boolean cover) throws Exception {
		var route = lodgingRepository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Lodging not found"));
		String ext = java.util.Optional.ofNullable(file.getOriginalFilename()).filter(f -> f.contains("."))
				.map(f -> f.substring(f.lastIndexOf('.') + 1)).orElse("jpg");
		String name = java.util.UUID.randomUUID() + "." + ext;
		String url = storage.upload("routes/" + id, name, file.getInputStream(), file.getContentType(), file.getSize());

		var img = new LodgingImage();
		img.setLodging(route);
		img.setUrl(url);
		img.setTitle(title);
		img.setAlt(alt);
		img.setCover(cover != null ? cover : Boolean.FALSE);
		img.setPosition(imageRepo.listByLodging(id).size());
		var saved = imageRepo.save(img);

		if (Boolean.TRUE.equals(img.getCover())) {
			imageRepo.listByLodging(id).forEach(other -> {
				if (!other.getLodgingImageId().equals(saved.getLodgingImageId()) && Boolean.TRUE.equals(other.getCover())) {
					other.setCover(false);
				}
			});
		}

		var dto = new ImageDto(saved.getLodgingImageId(), saved.getUrl(), saved.getTitle(), saved.getAlt(),
				saved.getCover(), saved.getPosition());
		return new UploadImageResponse(dto);
	}

	@PutMapping("/{id}/images/reorder")
	public java.util.List<ImageDto> reorder(@PathVariable Integer id, @RequestBody ReorderImagesRequest body) {
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

	@PutMapping("/{id}/images/{imageId}")
	public ImageDto updateMeta(@PathVariable Integer id, @PathVariable Integer imageId,
			@RequestBody UpdateImageMetaRequest body) {
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
					if (!other.getLodgingImageId().equals(imageId) && Boolean.TRUE.equals(other.getCover()))
						other.setCover(false);
				});
			}
		}
		return new ImageDto(img.getLodgingImageId(), img.getUrl(), img.getTitle(), img.getAlt(), img.getCover(),
				img.getPosition());
	}

	@DeleteMapping("/{id}/images/{imageId}")
	public org.springframework.http.ResponseEntity<Void> delete(@PathVariable Integer id,
			@PathVariable Integer imageId) {
		var img = imageRepo.findById(imageId)
				.orElseThrow(() -> new java.util.NoSuchElementException("Image not found"));
		imageRepo.delete(img);
		return org.springframework.http.ResponseEntity.noContent().build();
	}
}
