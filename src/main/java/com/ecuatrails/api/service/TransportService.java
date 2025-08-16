package com.ecuatrails.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.Directions;
import com.ecuatrails.api.dto.InterestPointTransportDto;
import com.ecuatrails.api.dto.RouteTransport;
import com.ecuatrails.api.helpers.converter.Mappers;
import com.ecuatrails.api.model.Route;
import com.ecuatrails.api.repository.InterestPointRepository;
import com.ecuatrails.api.repository.InterestPointTransportRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransportService {

	private final InterestPointTransportRepository iptRepository;
	private final InterestPointRepository ipRepository;
	private final RouteRepository routeRepository;

	public TransportService(InterestPointTransportRepository iptRepository, InterestPointRepository ipRepository,
			RouteRepository routeRepository) {
		this.iptRepository = iptRepository;
		this.ipRepository = ipRepository;
		this.routeRepository = routeRepository;
	}

	// GET /routes/{id}/transport
	public RouteTransport getByRoute(Integer routeId) {
		Route r = routeRepository.findById(routeId).orElseThrow();
		var poiIds = r.getInterestPoints().stream().map(p -> p.getInterestPointId()).toList();
		var items = iptRepository.findActiveByInterestPointIds(poiIds).stream()
				.map(Mappers::toInterestPointTransportDto) // ✅ correcto
				.toList();
		return new RouteTransport(routeId, items); // ✅ coincide con el record
	}

	// GET /interest-points/{id}/transport
	public List<InterestPointTransportDto> getByInterestPoint(Integer interestPointId) {
		ipRepository.findById(interestPointId).orElseThrow();
		return iptRepository.findActiveByInterestPoint(interestPointId).stream()
				.map(Mappers::toInterestPointTransportDto).toList();
	}

	// GET /directions
	public Directions directions(double originLat, double originLng, double destLat, double destLng, String mode) {
		double dMeters = haversineMeters(originLat, originLng, destLat, destLng);
		// average speed (approximate): walking 4.5 km/h, driving 40 km/h, transit 25
		// km/h
		double kmh = switch (mode == null ? "walking" : mode.toLowerCase()) {
		case "driving" -> 40.0;
		case "transit" -> 25.0;
		default -> 4.5;
		};
		int eta = (int) Math.round((dMeters / 1000.0) / kmh * 3600.0);
		var line = java.util.List.of(new double[] { originLng, originLat }, new double[] { destLng, destLat });
		return new Directions(originLat, originLng, destLat, destLng, mode, dMeters, eta, line);
	}

	private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
		double R = 6371000.0; // m
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}
}
