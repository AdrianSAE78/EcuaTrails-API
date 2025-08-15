package com.ecuatrails.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecuatrails.api.model.InterestPointImage;

public interface InterestPointImageRepository extends JpaRepository<InterestPointImage, Integer> {
	@Query("select i from InterestPointImage i where i.interestPoint.interestPointId = :id order by i.position asc, i.interestPointImageId asc")
	java.util.List<InterestPointImage> listByPoi(
			@org.springframework.data.repository.query.Param("id") Integer interestPointId);

	@Query("select i from InterestPointImage i where i.interestPoint.interestPointId = :id and i.cover = true")
	java.util.Optional<InterestPointImage> findCover(
			@org.springframework.data.repository.query.Param("id") Integer interestPointId);
}