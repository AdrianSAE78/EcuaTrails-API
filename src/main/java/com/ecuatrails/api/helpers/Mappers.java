package com.ecuatrails.api.helpers;

import java.util.List;
import java.util.stream.Collectors;

import com.ecuatrails.api.dto.AdminInterestPointDetail;
import com.ecuatrails.api.dto.AdminInterestPointList;
import com.ecuatrails.api.dto.AdminIptDto;
import com.ecuatrails.api.dto.AdminLodgingDetailDto;
import com.ecuatrails.api.dto.AdminLodgingListDto;
import com.ecuatrails.api.dto.AdminRouteDetailDto;
import com.ecuatrails.api.dto.AdminRouteListDto;
import com.ecuatrails.api.dto.AdminRouteLodgingLinkDto;
import com.ecuatrails.api.dto.AdminRoutePoiLinkDto;
import com.ecuatrails.api.dto.AdminTransportDetail;
import com.ecuatrails.api.dto.AdminTransportList;
import com.ecuatrails.api.dto.CategoryDto;
import com.ecuatrails.api.dto.CreateInterestPointRequest;
import com.ecuatrails.api.dto.CreateLodgingRequest;
import com.ecuatrails.api.dto.CreateTransportRequest;
import com.ecuatrails.api.dto.ImageDto;
import com.ecuatrails.api.dto.InterestPointDetail;
import com.ecuatrails.api.dto.InterestPointListItem;
import com.ecuatrails.api.dto.InterestPointTransportDto;
import com.ecuatrails.api.dto.LodgingDetail;
import com.ecuatrails.api.dto.LodgingDto;
import com.ecuatrails.api.dto.LodgingListItem;
import com.ecuatrails.api.dto.Poi;
import com.ecuatrails.api.dto.RouteCard;
import com.ecuatrails.api.dto.RouteDetail;
import com.ecuatrails.api.dto.RouteListItem;
import com.ecuatrails.api.dto.RouteMap;
import com.ecuatrails.api.dto.TransportDto;
import com.ecuatrails.api.dto.UpdateInterestPointRequest;
import com.ecuatrails.api.dto.UpdateLodgingRequest;
import com.ecuatrails.api.dto.UpdateTransportRequest;
import com.ecuatrails.api.model.Category;
import com.ecuatrails.api.model.InterestPoint;
import com.ecuatrails.api.model.InterestPointTransport;
import com.ecuatrails.api.model.Lodging;
import com.ecuatrails.api.model.Route;
import com.ecuatrails.api.model.RouteInterestPoint;
import com.ecuatrails.api.model.RouteLodging;
import com.ecuatrails.api.model.Transport;

public class Mappers {
	public static CategoryDto toCategoryDto(Category c) {
		return new CategoryDto(c.getCategoryId(), c.getCode(), c.getName());
	}

	public static RouteCard toCard(Route route) {
	    List<ImageDto> imageList = route.getImages().stream()
	        .map(routeImage -> new ImageDto(
	            routeImage.getRouteImageId(),
	            routeImage.getUrl(),
	            routeImage.getTitle(),
	            routeImage.getAlt(),
	            routeImage.getCover(),
	            routeImage.getPosition()
	        ))
	        .collect(Collectors.toList());
	    
	    return new RouteCard(
	        route.getRouteId(),
	        route.getName(),
	        route.getDescription(),
	        route.getCategory() != null ? route.getCategory().getName() : null,
	        imageList,
	        route.getEstimatedDuration(),
	        route.getDistance(),
	        route.getDifficulty()
	    );
	}

	public static RouteListItem toRouteListItem(Route r) {
		return new RouteListItem(r.getRouteId(), r.getName(), r.getDescription(),
				r.getCategory() != null ? r.getCategory().getName() : null, r.getEstimatedDuration(), r.getDistance(),
				r.getDifficulty());
	}

	public static Poi toPoi(InterestPoint p) {
		return new Poi(p.getInterestPointId(), p.getName(), p.getDescription(), p.getLatitude(), p.getLongitude(),
				p.getAddress());
	}

	public static LodgingDto toLodging(Lodging l) {
		return new LodgingDto(l.getLodgingId(), l.getName(), l.getDescription(), l.getApproximatePrice(),
				l.getLatitude(), l.getLongitude());
	}

	public static RouteDetail toRouteDetail(Route r) {
		var images = r.getImages().stream().map(img -> new ImageDto(img.getRouteImageId(), img.getUrl(), img.getTitle(),
				img.getAlt(), img.getCover(), img.getPosition())).toList();
		String cover = images.stream().filter(ImageDto::cover).findFirst().map(ImageDto::url).orElse(null);

		return new RouteDetail(r.getRouteId(), r.getName(), r.getDescription(),
				r.getCategory() != null ? r.getCategory().getName() : null, r.getEstimatedDuration(), r.getDistance(),
				r.getDifficulty(), r.getRecommendedSchedule(),
				r.getInterestPoints().stream().map(Mappers::toPoi).collect(Collectors.toList()),
				r.getLodgings().stream().map(Mappers::toLodging).collect(Collectors.toList()), images, cover);
	}

	public static RouteMap toMap(Route r) {
		var poiCoords = r.getInterestPoints().stream().map(p -> new double[] { p.getLongitude(), p.getLatitude() })
				.toList();
		var lodgingCoords = r.getLodgings().stream().map(l -> new double[] { l.getLongitude(), l.getLatitude() })
				.toList();
		return new RouteMap(r.getRouteId(), poiCoords, lodgingCoords);
	}

	public static TransportDto toTransportDto(Transport t) {
		return new TransportDto(t.getTransportId(), t.getName(), t.getType(), t.getRoute(), t.getBusLine(),
				t.getSchedule(), t.getBaseFare(), t.getAccessibility(), t.getStatus());
	}

	public static InterestPointTransportDto toInterestPointTransportDto(InterestPointTransport ipt) {
		return new InterestPointTransportDto(ipt.getInterestPointTransportId(),
				ipt.getInterestPoint().getInterestPointId(), ipt.getInterestPoint().getName(),
				ipt.getTransport().getTransportId(), ipt.getTransport().getName(), ipt.getWalkingDistanceMeters(),
				ipt.getEstimatedWalkingTime(), ipt.getAccessibilityNotes());
	}

	public static InterestPointListItem toInterestPointListItem(InterestPoint p) {
		return new InterestPointListItem(p.getInterestPointId(), p.getName(), p.getDescription(), p.getLatitude(),
				p.getLongitude());
	}

	public static InterestPointDetail toInterestPointDetail(InterestPoint p) {
		var images = p.getImages().stream().map(img -> new ImageDto(img.getInterestPointImageId(), img.getUrl(),
				img.getTitle(), img.getAlt(), img.getCover(), img.getPosition())).toList();
		String cover = images.stream().filter(ImageDto::cover).findFirst().map(ImageDto::url).orElse(null);

		return new InterestPointDetail(p.getInterestPointId(), p.getName(), p.getDescription(), p.getLatitude(),
				p.getLongitude(), p.getAddress(), p.getCity(), p.getOpeningHours(), p.getRating(), p.getReviewCount(),
				images, cover);
	}

	public static LodgingListItem toLodgingListItem(Lodging l) {
		return new LodgingListItem(l.getLodgingId(), l.getName(), l.getDescription(), l.getApproximatePrice(),
				l.getLatitude(), l.getLongitude());
	}

	public static LodgingDetail toLodgingDetail(Lodging l) {
		var images = l.getImages().stream().map(img -> new ImageDto(img.getLodgingImageId(), img.getUrl(),
				img.getTitle(), img.getAlt(), img.getCover(), img.getPosition())).toList();
		String cover = images.stream().filter(ImageDto::cover).findFirst().map(ImageDto::url).orElse(null);

		return new LodgingDetail(l.getLodgingId(), l.getName(), l.getDescription(), l.getApproximatePrice(),
				l.getLatitude(), l.getLongitude(), images, cover);
	}

	public static AdminInterestPointList toAdminInterestPointList(InterestPoint p) {
		return new AdminInterestPointList(p.getInterestPointId(), p.getName(), p.getCity(), p.getStatus(),
				p.getLatitude(), p.getLongitude());
	}

	public static AdminInterestPointDetail toAdminInterestPointDetail(InterestPoint p) {
		return new AdminInterestPointDetail(p.getInterestPointId(), p.getName(), p.getDescription(), p.getAddress(),
				p.getCity(), p.getOpeningHours(), p.getRating(), p.getReviewCount(), p.getLatitude(), p.getLongitude(),
				p.getStatus());
	}

	public static void AdminIpdApply(InterestPoint p, CreateInterestPointRequest r) {
		p.setName(r.name());
		p.setDescription(r.description());
		p.setAddress(r.address());
		p.setCity(r.city());
		p.setOpeningHours(r.openingHours());
		p.setRating(r.rating());
		p.setReviewCount(r.reviewCount());
		p.setLatitude(r.latitude());
		p.setLongitude(r.longitude());
		if (r.status() != null)
			p.setStatus(r.status());
	}

	public static void AdminIpdApply(InterestPoint p, UpdateInterestPointRequest r) {
		if (r.name() != null)
			p.setName(r.name());
		if (r.description() != null)
			p.setDescription(r.description());
		if (r.address() != null)
			p.setAddress(r.address());
		if (r.city() != null)
			p.setCity(r.city());
		if (r.openingHours() != null)
			p.setOpeningHours(r.openingHours());
		if (r.rating() != null)
			p.setRating(r.rating());
		if (r.reviewCount() != null)
			p.setReviewCount(r.reviewCount());
		if (r.latitude() != null)
			p.setLatitude(r.latitude());
		if (r.longitude() != null)
			p.setLongitude(r.longitude());
		if (r.status() != null)
			p.setStatus(r.status());
	}

	public static AdminTransportList toAdminTransportList(Transport t) {
		return new AdminTransportList(t.getTransportId(), t.getName(), t.getType(), t.getRoute(), t.getBusLine(),
				t.getBaseFare(), t.getAccessibility(), t.getStatus());
	}

	public static AdminTransportDetail toAdminTransportDetail(Transport t) {
		return new AdminTransportDetail(t.getTransportId(), t.getName(), t.getType(), t.getRoute(), t.getBusLine(),
				t.getSchedule(), t.getBaseFare(), t.getAccessibility(), t.getStatus());
	}

	public static void AdminTransportApply(Transport t, CreateTransportRequest r) {
		t.setName(r.name());
		t.setType(r.type());
		t.setRoute(r.route());
		t.setBusLine(r.busLine());
		t.setSchedule(r.schedule());
		t.setBaseFare(r.baseFare());
		if (r.accessibility() != null)
			t.setAccessibility(r.accessibility());
		if (r.status() != null)
			t.setStatus(r.status());
	}

	public static void AdminTransportApply(Transport t, UpdateTransportRequest r) {
		if (r.name() != null)
			t.setName(r.name());
		if (r.type() != null)
			t.setType(r.type());
		if (r.route() != null)
			t.setRoute(r.route());
		if (r.busLine() != null)
			t.setBusLine(r.busLine());
		if (r.schedule() != null)
			t.setSchedule(r.schedule());
		if (r.baseFare() != null)
			t.setBaseFare(r.baseFare());
		if (r.accessibility() != null)
			t.setAccessibility(r.accessibility());
		if (r.status() != null)
			t.setStatus(r.status());
	}

	public static AdminIptDto toAdminIptLink(InterestPointTransport ipt) {
		return new AdminIptDto(ipt.getInterestPointTransportId(), ipt.getInterestPoint().getInterestPointId(),
				ipt.getInterestPoint().getName(), ipt.getTransport().getTransportId(), ipt.getTransport().getName(),
				ipt.getWalkingDistanceMeters(), ipt.getEstimatedWalkingTime(), ipt.getAccessibilityNotes(),
				ipt.getStatus());
	}

	public static AdminRouteListDto toAdminRouteList(Route r) {
		return new AdminRouteListDto(r.getRouteId(), r.getName(),
				r.getCategory() != null ? r.getCategory().getName() : null, r.getDifficulty(), r.getDistance(),
				r.getStatus());
	}

	public static AdminRouteDetailDto toAdminRouteDetail(Route r) {
		return new AdminRouteDetailDto(r.getRouteId(), r.getName(), r.getDescription(),
				r.getCategory() != null ? r.getCategory().getCategoryId() : null, r.getEstimatedDuration(),
				r.getRecommendedSchedule(), r.getDistance(), r.getDifficulty(), r.getStatus());
	}

	public static AdminRoutePoiLinkDto toAdminRoutePoiLink(RouteInterestPoint rip) {
		return new AdminRoutePoiLinkDto(rip.getRouteInterestPointId(), rip.getInterestPoint().getInterestPointId(),
				rip.getInterestPoint().getName(), rip.getPosition());
	}

	public static AdminRouteLodgingLinkDto toAdminRouteLodgingLink(RouteLodging rl) {
		return new AdminRouteLodgingLinkDto(rl.getRouteLodgingId(), rl.getLodging().getLodgingId(),
				rl.getLodging().getName());
	}

	public static AdminLodgingListDto toAdminLodgingList(Lodging l) {
		return new AdminLodgingListDto(l.getLodgingId(), l.getName(), l.getDescription(), l.getApproximatePrice(),
				l.getLatitude(), l.getLongitude(), l.getStatus());
	}

	public static AdminLodgingDetailDto toAdminLodgingDetail(Lodging l) {
		return new AdminLodgingDetailDto(l.getLodgingId(), l.getName(), l.getDescription(), l.getApproximatePrice(),
				l.getLatitude(), l.getLongitude(), l.getStatus());
	}

	public static void applyAdminLodgingCreate(Lodging l, CreateLodgingRequest r) {
		l.setName(r.name());
		l.setDescription(r.description());
		l.setApproximatePrice(r.approximatePrice());
		l.setLatitude(r.latitude());
		l.setLongitude(r.longitude());
		if (r.status() != null)
			l.setStatus(r.status());
	}

	public static void applyAdminLodgingUpdate(Lodging l, UpdateLodgingRequest r) {
		if (r.name() != null)
			l.setName(r.name());
		if (r.description() != null)
			l.setDescription(r.description());
		if (r.approximatePrice() != null)
			l.setApproximatePrice(r.approximatePrice());
		if (r.latitude() != null)
			l.setLatitude(r.latitude());
		if (r.longitude() != null)
			l.setLongitude(r.longitude());
		if (r.status() != null)
			l.setStatus(r.status());
	}
}

