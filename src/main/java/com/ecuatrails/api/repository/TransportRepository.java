package com.ecuatrails.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.Transport;

public interface TransportRepository extends JpaRepository<Transport, Integer> {

	@Query("""
			  select t from Transport t
			  where (:q is null or lower(t.name) like lower(concat('%', :q, '%'))
			                 or lower(t.route) like lower(concat('%', :q, '%'))
			                 or lower(t.busLine) like lower(concat('%', :q, '%')))
			    and (:type is null or t.type = :type)
			    and (:active is null or t.status = :active)
			  order by t.name asc
			""")
	Page<Transport> search(@Param("q") String q, @Param("type") String type, @Param("active") Boolean active,
			Pageable pageable);

	@Query("select count(ipt) from InterestPointTransport ipt where ipt.transport.transportId = :transportId")
	long countLinks(@Param("transportId") Integer transportId);
}
