package com.ecuatrails.api.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.Lodging;

public interface LodgingRepository extends JpaRepository<Lodging, Integer> {

	@Query("""
			  select l from Lodging l
			  where l.status = true
			    and (:q is null or lower(l.name) like lower(concat('%', :q, '%'))
			                 or lower(l.description) like lower(concat('%', :q, '%')))
			    and (:minPrice is null or l.approximatePrice >= :minPrice)
			    and (:maxPrice is null or l.approximatePrice <= :maxPrice)
			  order by l.name asc
			""")
	Page<Lodging> search(@Param("q") String q, @Param("minPrice") BigDecimal minPrice,
			@Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

	@Query("""
			  select l from Lodging l
			  where (:q is null or lower(l.name) like lower(concat('%', :q, '%'))
			                 or lower(l.description) like lower(concat('%', :q, '%')))
			    and (:active is null or l.status = :active)
			    and (:minPrice is null or l.approximatePrice >= :minPrice)
			    and (:maxPrice is null or l.approximatePrice <= :maxPrice)
			  order by l.name asc
			""")
	Page<Lodging> searchByStatus(@Param("q") String q, @Param("active") Boolean active,
			@Param("minPrice") java.math.BigDecimal minPrice, @Param("maxPrice") java.math.BigDecimal maxPrice,
			Pageable pageable);
}
