package com.ecuatrails.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.InterestPoint;

public interface InterestPointRepository extends JpaRepository<InterestPoint, Integer> {

	  @Query("""
	    select distinct p
	    from Route r
	      join r.interestPoints p
	    where r.routeId = :routeId
	      and p.Status = true
	    order by p.name asc
	  """)
	  List<InterestPoint> findActiveByRoute(@Param("routeId") Integer routeId);

	  @Query("""
	    select p
	    from InterestPoint p
	    where p.interestPointId = :id and p.Status = true
	  """)
	  Optional<InterestPoint> findActiveById(@Param("id") Integer id);

	  @Query("""
	    select p from InterestPoint p
	    where (:q is null
	           or p.name        ilike concat('%', cast(:q as string), '%')
	           or p.description ilike concat('%', cast(:q as string), '%')
	           or p.city        ilike concat('%', cast(:q as string), '%'))
	      and (:active is null or p.Status = :active)
	    order by p.name asc
	  """)
	  Page<InterestPoint> search(@Param("q") String q,
	                             @Param("active") Boolean active,
	                             Pageable pageable);
	}

