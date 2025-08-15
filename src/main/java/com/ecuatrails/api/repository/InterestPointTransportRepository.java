package com.ecuatrails.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.InterestPointTransport;

public interface InterestPointTransportRepository extends JpaRepository<InterestPointTransport, Integer> {

	@Query("""
			  select ipt from InterestPointTransport ipt
			    join fetch ipt.transport t
			    join fetch ipt.interestPoint p
			  where ipt.status = true
			    and p.interestPointId = :interestPointId
			    and t.status = true
			""")
	List<InterestPointTransport> findActiveByInterestPoint(@Param("interestPointId") Integer interestPointId);

	@Query("""
			  select ipt from InterestPointTransport ipt
			    join fetch ipt.transport t
			    join fetch ipt.interestPoint p
			  where ipt.status = true
			    and p.interestPointId in :interestPointIds
			    and t.status = true
			""")
	List<InterestPointTransport> findActiveByInterestPointIds(
			@Param("interestPointIds") Collection<Integer> interestPointIds);

	@Query("select count(ipt) from InterestPointTransport ipt where ipt.interestPoint.interestPointId = :poiId")
	long countLinks(@Param("poiId") Integer poiId);

	@Query("""
			  select ipt from InterestPointTransport ipt
			    join fetch ipt.transport t
			    join fetch ipt.interestPoint p
			  where p.interestPointId = :poiId
			""")
	java.util.List<InterestPointTransport> findByPoi(@Param("poiId") Integer poiId);

	@Query("""
			  select ipt from InterestPointTransport ipt
			  where ipt.interestPoint.interestPointId = :poiId
			    and ipt.interestPointTransportId = :iptId
			""")
	java.util.Optional<InterestPointTransport> findOneForPoi(@Param("poiId") Integer poiId,
			@Param("iptId") Integer iptId);
}
