package com.ecuatrails.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.RouteLodging;

public interface RouteLodgingRepository extends JpaRepository<RouteLodging, Integer> {

	@Query("""
			  select rl from RouteLodging rl
			    join fetch rl.lodging l
			  where rl.route.routeId = :routeId
			  order by l.name asc
			""")
	List<RouteLodging> listByRoute(@Param("routeId") Integer routeId);

	@Query("""
			  select rl from RouteLodging rl
			  where rl.route.routeId = :routeId and rl.routeLodgingId = :rlId
			""")
	Optional<RouteLodging> findOneForRoute(@Param("routeId") Integer routeId, @Param("rlId") Integer rlId);
}
