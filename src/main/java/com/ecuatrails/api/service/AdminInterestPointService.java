package com.ecuatrails.api.service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.AdminInterestPointDetail;
import com.ecuatrails.api.dto.AdminInterestPointList;
import com.ecuatrails.api.dto.BulkInterestPointRequest;
import com.ecuatrails.api.dto.CreateInterestPointRequest;
import com.ecuatrails.api.dto.GeocodeResponse;
import com.ecuatrails.api.dto.UpdateInterestPointRequest;
import com.ecuatrails.api.helpers.Mappers;
import com.ecuatrails.api.model.InterestPoint;
import com.ecuatrails.api.repository.InterestPointRepository;
import com.ecuatrails.api.repository.InterestPointTransportRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminInterestPointService {

	private final InterestPointRepository ipRepository;
	private final RouteRepository routeRepository;
	private final InterestPointTransportRepository iptRepository;
	private final GeoService geoService;

	public AdminInterestPointService(InterestPointRepository ipRepository, RouteRepository routeRepository,
			InterestPointTransportRepository iptRepository, GeoService geoService) {
		this.ipRepository = ipRepository;
		this.routeRepository = routeRepository;
		this.iptRepository = iptRepository;
		this.geoService = geoService;
	}

	public Page<AdminInterestPointList> list(String q, Boolean status, int page, int size) {
		var pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		return ipRepository.search(q, status, pageable).map(Mappers::toAdminInterestPointList);
	}

	public AdminInterestPointDetail get(Integer id) {
		InterestPoint p = ipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		return Mappers.toAdminInterestPointDetail(p);
	}

	public AdminInterestPointDetail create(CreateInterestPointRequest r) {
		var p = new InterestPoint();
		Mappers.AdminIpdApply(p, r);
		var saved = ipRepository.save(p);
		return Mappers.toAdminInterestPointDetail(saved);
	}

	public AdminInterestPointDetail update(Integer id, UpdateInterestPointRequest r) {
		var p = ipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		Mappers.AdminIpdApply(p, r);
		return Mappers.toAdminInterestPointDetail(p);
	}

	public void delete(Integer id) {
		var p = ipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		long usedByRoutes = routeRepository.countByInterestPoint(id);
		long usedByTransport = iptRepository.countLinks(id);
		if (usedByRoutes > 0 || usedByTransport > 0) {
			throw new IllegalStateException(
					"POI is in use (routes=" + usedByRoutes + ", transports=" + usedByTransport + ")");
		}
		ipRepository.delete(p);
	}

	public java.util.List<AdminInterestPointDetail> bulkCreate(BulkInterestPointRequest body) {
		if (body == null || body.items() == null || body.items().isEmpty())
			return java.util.List.of();
		var toSave = body.items().stream().map(r -> {
			var p = new InterestPoint();
			Mappers.AdminIpdApply(p, r);
			return p;
		}).collect(Collectors.toList());
		return ipRepository.saveAll(toSave).stream().map(Mappers::toAdminInterestPointDetail).toList();
	}

	public GeocodeResponse geocode(Integer id) {
		var p = ipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		var coords = geoService.geocode(p.getAddress(), p.getCity());
		if (coords != null) {
			p.setLatitude(coords[0]);
			p.setLongitude(coords[1]);
			return new GeocodeResponse(coords[0], coords[1]);
		}
		throw new IllegalStateException("Geocoding failed");
	}

	public AdminInterestPointDetail setStatus(Integer id, boolean status) {
		var p = ipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
		p.setStatus(status);
		return Mappers.toAdminInterestPointDetail(p);
	}

	// ---- GeoService ----
	public interface GeoService {
		Float[] geocode(String address, String city);
	}
}
