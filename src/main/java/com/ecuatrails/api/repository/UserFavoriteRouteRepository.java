package com.ecuatrails.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.UserFavoriteRoute;

public interface UserFavoriteRouteRepository extends JpaRepository<UserFavoriteRoute, Integer> {

	@Query("""
			  select f from UserFavoriteRoute f
			    join fetch f.route r
			  where f.user.userId = :userId
			  order by f.created desc
			""")
	Page<UserFavoriteRoute> findByUser(@Param("userId") Integer userId, Pageable pageable);

	@Query("""
			  select f from UserFavoriteRoute f
			  where f.user.userId = :userId and f.route.routeId = :routeId
			""")
	Optional<UserFavoriteRoute> findByUserAndRoute(@Param("userId") Integer userId, @Param("routeId") Integer routeId);

	@Query("""
			  select f from UserFavoriteRoute f
			  where f.user.userId = :userId and f.userFavoriteRouteId = :favoriteId
			""")
	Optional<UserFavoriteRoute> findOneForUser(@Param("userId") Integer userId, @Param("favoriteId") Integer favoriteId);
}
