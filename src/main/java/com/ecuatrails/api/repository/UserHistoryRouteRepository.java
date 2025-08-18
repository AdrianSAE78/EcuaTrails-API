package com.ecuatrails.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.UserHistoryRoute;

public interface UserHistoryRouteRepository extends JpaRepository<UserHistoryRoute, Integer> {

	@Query("""
			 select h from UserHistoryRoute h
			 where h.user.userId = :userId and h.route.routeId = :routeId
			 order by h.routeDate desc
			""")
	List<UserHistoryRoute> findAllByUserAndRoute(@Param("userId") Integer userId, @Param("routeId") Integer routeId);

	@Query("""
			  select h from UserHistoryRoute h
			  where h.user.userId = :userId and h.route.routeId = :routeId
			    and h.isFinished = false
			  order by h.routeDate desc
			""")
	List<UserHistoryRoute> findActives(@Param("userId") Integer userId, @Param("routeId") Integer routeId);

	@Query("""
			  select h from UserHistoryRoute h
			  where h.user.userId = :userId and h.route.routeId = :routeId
			    and h.isFinished = true and h.routeDate >= :since
			  order by h.routeDate desc
			""")
	List<UserHistoryRoute> findFinishedSince(@Param("userId") Integer userId, @Param("routeId") Integer routeId,
			@Param("since") LocalDateTime since);

	@Query("""
			    select distinct h.route.routeId
			    from UserHistoryRoute h
			    where h.user.userId = :userId
			      and h.isFinished = true
			      and h.routeDate >= :cutoff
			""")
	List<Integer> findFinishedRouteIdsSince(@Param("userId") Integer userId, @Param("cutoff") LocalDateTime cutoff);

	@Query("""
			  select h from UserHistoryRoute h
			    join fetch h.route r
			  where h.user.userId = :userId
			  order by h.routeDate desc
			""")
	Page<UserHistoryRoute> findByUser(@Param("userId") Integer userId, Pageable pageable);

	@Query("""
			  select h from UserHistoryRoute h
			  where h.user.userId = :userId and h.userHistoryRouteId = :historyId
			""")
	Optional<UserHistoryRoute> findOneByUserAndId(@Param("userId") Integer userId,
			@Param("historyId") Integer historyId);

	@Query("""
			  select count(h)
			  from UserHistoryRoute h
			  where h.user.userId = :userId and h.isFinished = true
			""")
	long countFinished(@Param("userId") Integer userId);

	@Query("""
			  select coalesce(sum(r.distance), 0)
			  from UserHistoryRoute h
			    join h.route r
			  where h.user.userId = :userId and h.isFinished = true
			""")
	Float sumDistanceFinished(@Param("userId") Integer userId);

	@Query("""
			  select coalesce(sum(r.estimatedDuration), 0)
			  from UserHistoryRoute h
			    join h.route r
			  where h.user.userId = :userId and h.isFinished = true
			""")
	Long sumDurationSecondsFinished(@Param("userId") Integer userId);

	@Query("""
			  select h from UserHistoryRoute h
			  where h.user.userId = :userId
			  order by h.routeDate desc
			""")
	java.util.List<UserHistoryRoute> findRecent(@Param("userId") Integer userId,
			org.springframework.data.domain.Pageable pageable);
}
