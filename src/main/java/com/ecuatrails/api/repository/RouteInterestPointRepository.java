package com.ecuatrails.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.RouteInterestPoint;

public interface RouteInterestPointRepository extends JpaRepository<RouteInterestPoint, Integer> {

	@Query("""
			  select rip from RouteInterestPoint rip
			    join fetch rip.interestPoint p
			  where rip.route.routeId = :routeId
			  order by rip.position asc, p.name asc
			""")
	List<RouteInterestPoint> listByRoute(@Param("routeId") Integer routeId);

	@Query("""
			  select rip from RouteInterestPoint rip
			  where rip.route.routeId = :routeId and rip.routeInterestPointId = :ripId
			""")
	Optional<RouteInterestPoint> findOneForRoute(@Param("routeId") Integer routeId, @Param("ripId") Integer ripId);
}
