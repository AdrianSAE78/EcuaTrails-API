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
			  where (:term is null or l.name ilike :term or l.description ilike :term)
			    and (:active is null or l.status = :active)
			    and (:minPrice is null or l.approximatePrice >= :minPrice)
			    and (:maxPrice is null or l.approximatePrice <= :maxPrice)
			  order by l.name asc
			""")
			Page<Lodging> searchByStatus(@Param("term") String term,
			                             @Param("active") Boolean active,
			                             @Param("minPrice") BigDecimal minPrice,
			                             @Param("maxPrice") BigDecimal maxPrice,
			                             Pageable pageable);

			@Query("""
			  select l from Lodging l
			  where l.status = true
			    and (:term is null or l.name ilike :term or l.description ilike :term)
			    and (:minPrice is null or l.approximatePrice >= :minPrice)
			    and (:maxPrice is null or l.approximatePrice <= :maxPrice)
			  order by l.name asc
			""")
			Page<Lodging> search(@Param("term") String term,
			                     @Param("minPrice") BigDecimal minPrice,
			                     @Param("maxPrice") BigDecimal maxPrice,
			                     Pageable pageable);

}
