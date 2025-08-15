package com.ecuatrails.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecuatrails.api.model.RouteImage;

public interface RouteImageRepository extends JpaRepository<RouteImage, Integer> {
	@Query("select i from RouteImage i where i.route.routeId = :routeId order by i.position asc, i.routeImageId asc")
	java.util.List<RouteImage> listByRoute(@org.springframework.data.repository.query.Param("routeId") Integer routeId);

	@Query("select i from RouteImage i where i.route.routeId = :routeId and i.cover = true")
	java.util.Optional<RouteImage> findCover(
			@org.springframework.data.repository.query.Param("routeId") Integer routeId);
}