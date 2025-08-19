package com.ecuatrails.api.repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.Lodging;
import com.ecuatrails.api.model.Route;

public interface RouteRepository extends JpaRepository<Route, Integer> {

	@Query("""
			select distinct r
			from Route r
			left join fetch r.images i
			left join fetch r.category c
			where r.status = true
			and (:categoryId is null or r.category.categoryId = :categoryId)
			and (:maxDuration is null or r.estimatedDuration <= :maxDuration)
			and (coalesce(:excludeIds, null) is null or r.routeId not in :excludeIds)
			order by r.created desc
			""")
			List<Route> findRecommended(@Param("categoryId") Integer categoryId, 
			                           @Param("maxDuration") Duration maxDuration,
			                           @Param("excludeIds") List<Integer> excludeIds);

	@Query("""
		    select r
		    from Route r
		    left join fetch r.images i
		    where r.status = true
		      and (:categoryId is null or r.category.categoryId = :categoryId)
		      and (:difficulty is null or r.difficulty = :difficulty)
		      and (:term is null or r.name ilike :term or r.description ilike :term)
		    order by r.created desc
		  """)
		  Page<Route> search(@Param("categoryId") Integer categoryId,
		                     @Param("difficulty") String difficulty,
		                     @Param("term") String term,
		                     Pageable pageable);

	@Query("""
		    select r
		    from Route r
		    left join fetch r.images i
		    where r.routeId = :routeId and r.status = true
		    order by i.position asc, i.routeImageId asc
		""")
		List<Lodging> findActiveLodgingsByRoute(@Param("routeId") Integer routeId);

	@Query("select count(r) from Route r where r.category.categoryId = :categoryId")
	long countByCategoryId(@Param("categoryId") Integer categoryId);

	@Query("""
			  select count(r) from Route r join r.interestPoints ip
			  where ip.interestPointId = :poiId
			""")
	long countByInterestPoint(@Param("poiId") Integer poiId);

	@Query("""
		    select r
		      from Route r
		      left join r.category c
		    where (:term is null or r.name ilike :term or r.description ilike :term)
		      and (:categoryId is null or c.categoryId = :categoryId)
		      and (:status is null or r.status = :status)
		    order by r.created desc
		  """)
		  Page<Route> searchByStatus(@Param("term") String term,
		                             @Param("categoryId") Integer categoryId,
		                             @Param("status") Boolean status,
		                             Pageable pageable);
	
	@Query("""
			select r from Route r
			left join fetch r.images i
			where r.routeId = :routeId
			""")
	Optional<Route> findByIdWithImages(@Param("routeId") Integer routeId);

	@Query("select count(l) from Route r join r.lodgings l where r.routeId = :routeId")
	long countLodgings(@Param("routeId") Integer routeId);

	@Query("select count(p) from Route r join r.interestPoints p where r.routeId = :routeId")
	long countPois(@Param("routeId") Integer routeId);

	@Query("select count(r) from Route r join r.lodgings l where l.lodgingId = :lodgingId")
	long countRoutesUsingLodging(@Param("lodgingId") Integer lodgingId);
}

