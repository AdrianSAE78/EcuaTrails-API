package com.ecuatrails.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.Transport;

public interface TransportRepository extends JpaRepository<Transport, Integer> {

	@Query("""
			  select t
			    from Transport t
			  where (:term is null
			          or t.name ilike :term
			          or t.route ilike :term
			          or t.busLine ilike :term)
			    and (:type is null or t.type = :type)
			    and (:active is null or t.status = :active)
			  order by t.name asc
			""")
	Page<Transport> search(@Param("term") String term, @Param("type") String type, @Param("active") Boolean active,
			Pageable pageable);

	@Query("select count(ipt) from InterestPointTransport ipt where ipt.transport.transportId = :transportId")
	long countLinks(@Param("transportId") Integer transportId);
}
