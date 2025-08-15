package com.ecuatrails.api.service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.AdminIptDto;
import com.ecuatrails.api.dto.AdminTransportDetail;
import com.ecuatrails.api.dto.AdminTransportList;
import com.ecuatrails.api.dto.CreateIptLinkRequest;
import com.ecuatrails.api.dto.CreateTransportRequest;
import com.ecuatrails.api.dto.UpdateTransportRequest;
import com.ecuatrails.api.helpers.converter.Mappers;
import com.ecuatrails.api.model.InterestPointTransport;
import com.ecuatrails.api.model.Transport;
import com.ecuatrails.api.repository.InterestPointRepository;
import com.ecuatrails.api.repository.InterestPointTransportRepository;
import com.ecuatrails.api.repository.TransportRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminTransportService {

	private final TransportRepository transportRepository;
	private final InterestPointRepository poiRepository;
	private final InterestPointTransportRepository iptRepository;

	public AdminTransportService(TransportRepository transportRepository, InterestPointRepository poiRepository,
			InterestPointTransportRepository iptRepository) {
		this.transportRepository = transportRepository;
		this.poiRepository = poiRepository;
		this.iptRepository = iptRepository;
	}

	public Page<AdminTransportList> list(String q, String type, Boolean status, int page, int size) {
		var pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		return transportRepository.search(q, type, status, pageable).map(Mappers::toAdminTransportList);
	}

	public AdminTransportDetail create(CreateTransportRequest r) {
		var t = new Transport();
		Mappers.AdminTransportApply(t, r);
		var saved = transportRepository.save(t);
		return Mappers.toAdminTransportDetail(saved);
	}

	public AdminTransportDetail update(Integer id, UpdateTransportRequest r) {
		var t = transportRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Transport not found"));
		Mappers.AdminTransportApply(t, r);
		return Mappers.toAdminTransportDetail(t);
	}

	public void delete(Integer id) {
		var t = transportRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Transport not found"));
		long links = transportRepository.countLinks(id);
		if (links > 0)
			throw new IllegalStateException("Transport is linked to " + links + " interest points");
		transportRepository.delete(t);
	}

	public java.util.List<AdminIptDto> listByPoi(Integer poiId) {
		poiRepository.findById(poiId).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		return iptRepository.findByPoi(poiId).stream().map(Mappers::toAdminIptLink).collect(Collectors.toList());
	}

	public AdminIptDto link(Integer poiId, CreateIptLinkRequest r) {
		var poi = poiRepository.findById(poiId).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		var t = transportRepository.findById(r.transportId())
				.orElseThrow(() -> new NoSuchElementException("Transport not found"));
		var ipt = new InterestPointTransport();
		ipt.setInterestPoint(poi);
		ipt.setTransport(t);
		ipt.setWalkingDistanceMeters(r.walkingDistanceMeters());
		ipt.setEstimatedWalkingTime(r.estimatedWalkingTime());
		ipt.setAccessibilityNotes(r.accessibilityNotes());
		ipt.setStatus(r.status() != null ? r.status() : Boolean.TRUE);
		var saved = iptRepository.save(ipt);
		return Mappers.toAdminIptLink(saved);
	}

	public void unlink(Integer poiId, Integer iptId) {
		var ipt = iptRepository.findOneForPoi(poiId, iptId).orElseThrow(() -> new NoSuchElementException("Link not found"));
		iptRepository.delete(ipt);
	}
}
