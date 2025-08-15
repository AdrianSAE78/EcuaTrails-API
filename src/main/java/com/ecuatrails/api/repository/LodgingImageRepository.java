package com.ecuatrails.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecuatrails.api.model.LodgingImage;

public interface LodgingImageRepository extends JpaRepository<LodgingImage, Integer> {
	@Query("select i from LodgingImage i where i.lodging.lodgingId = :id order by i.position asc, i.lodgingImageId asc")
	java.util.List<LodgingImage> listByLodging(
			@org.springframework.data.repository.query.Param("id") Integer lodgingId);

	@Query("select i from LodgingImage i where i.lodging.lodgingId = :id and i.cover = true")
	java.util.Optional<LodgingImage> findCover(
			@org.springframework.data.repository.query.Param("id") Integer lodgingId);
}
