package com.ecuatrails.api.service;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.dto.AdminRouteDetailDto;
import com.ecuatrails.api.dto.AdminRouteListDto;
import com.ecuatrails.api.dto.AdminRouteLodgingLinkDto;
import com.ecuatrails.api.dto.AdminRoutePoiLinkDto;
import com.ecuatrails.api.dto.CreateRouteLodgingLinkRequest;
import com.ecuatrails.api.dto.CreateRoutePoiLinkRequest;
import com.ecuatrails.api.dto.CreateRouteRequest;
import com.ecuatrails.api.dto.ReorderRoutePoiRequest;
import com.ecuatrails.api.dto.UpdateRouteRequest;
import com.ecuatrails.api.helpers.Mappers;
import com.ecuatrails.api.model.Route;
import com.ecuatrails.api.model.RouteInterestPoint;
import com.ecuatrails.api.model.RouteLodging;
import com.ecuatrails.api.repository.CategoryRepository;
import com.ecuatrails.api.repository.InterestPointRepository;
import com.ecuatrails.api.repository.LodgingRepository;
import com.ecuatrails.api.repository.RouteInterestPointRepository;
import com.ecuatrails.api.repository.RouteLodgingRepository;
import com.ecuatrails.api.repository.RouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminRouteService {

  private final RouteRepository routeRepo;
  private final CategoryRepository categoryRepo;
  private final InterestPointRepository poiRepo;
  private final LodgingRepository lodgingRepo;
  private final RouteInterestPointRepository ripRepo;
  private final RouteLodgingRepository rlRepo;

  public AdminRouteService(RouteRepository routeRepo,
                           CategoryRepository categoryRepo,
                           InterestPointRepository poiRepo,
                           LodgingRepository lodgingRepo,
                           RouteInterestPointRepository ripRepo,
                           RouteLodgingRepository rlRepo) {
    this.routeRepo = routeRepo;
    this.categoryRepo = categoryRepo;
    this.poiRepo = poiRepo;
    this.lodgingRepo = lodgingRepo;
    this.ripRepo = ripRepo;
    this.rlRepo = rlRepo;
  }

  // --- routes ---

  public Page<AdminRouteListDto> list(String q, Integer categoryId, Boolean status, int page, int size) {
    var pageable = PageRequest.of(page, size);
    return routeRepo.searchByStatus(q, categoryId, status, pageable).map(Mappers::toAdminRouteList);
  }

  public AdminRouteDetailDto create(CreateRouteRequest r) {
    var route = new Route();
    route.setName(r.name());
    route.setDescription(r.description());
    if (r.categoryId() != null) {
      var c = categoryRepo.findById(r.categoryId()).orElseThrow(() -> new NoSuchElementException("Category not found"));
      route.setCategory(c);
    }
    route.setEstimatedDuration(r.estimatedDuration());
    route.setRecommendedSchedule(r.recommendedSchedule());
    route.setDistance(r.distance());
    route.setDifficulty(r.difficulty());
    if (r.status() != null) route.setStatus(r.status());
    return Mappers.toAdminRouteDetail(routeRepo.save(route));
  }

  public AdminRouteDetailDto update(Integer id, UpdateRouteRequest r) {
    var route = routeRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Route not found"));
    if (r.name() != null) route.setName(r.name());
    if (r.description() != null) route.setDescription(r.description());
    if (r.categoryId() != null) {
      var c = categoryRepo.findById(r.categoryId()).orElseThrow(() -> new NoSuchElementException("Category not found"));
      route.setCategory(c);
    }
    if (r.estimatedDuration() != null) route.setEstimatedDuration(r.estimatedDuration());
    if (r.recommendedSchedule() != null) route.setRecommendedSchedule(r.recommendedSchedule());
    if (r.distance() != null) route.setDistance(r.distance());
    if (r.difficulty() != null) route.setDifficulty(r.difficulty());
    if (r.status() != null) route.setStatus(r.status());
    return Mappers.toAdminRouteDetail(route);
  }

  public void delete(Integer id) {
    var route = routeRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Route not found"));
    long pois = routeRepo.countPois(id);
    long lodgings = routeRepo.countLodgings(id);
    if (pois > 0 || lodgings > 0) {
      throw new IllegalStateException("Route has relations (pois=" + pois + ", lodgings=" + lodgings + ")");
    }
    routeRepo.delete(route);
  }

  // --- POIs ---

  public java.util.List<AdminRoutePoiLinkDto> listPois(Integer routeId) {
    ensureRoute(routeId);
    return ripRepo.listByRoute(routeId).stream().map(Mappers::toAdminRoutePoiLink).collect(Collectors.toList());
  }

  public AdminRoutePoiLinkDto addPoi(Integer routeId, CreateRoutePoiLinkRequest r) {
    var route = ensureRoute(routeId);
    var poi = poiRepo.findById(r.interestPointId()).orElseThrow(() -> new NoSuchElementException("Interest point not found"));
    var link = new RouteInterestPoint();
    link.setRoute(route);
    link.setInterestPoint(poi);
    link.setPosition(r.position() != null ? r.position() : 0);
    return Mappers.toAdminRoutePoiLink(ripRepo.save(link));
  }

  public void deletePoi(Integer routeId, Integer routeInterestPointId) {
    var link = ripRepo.findOneForRoute(routeId, routeInterestPointId)
        .orElseThrow(() -> new NoSuchElementException("Link not found"));
    ripRepo.delete(link);
  }

  public java.util.List<AdminRoutePoiLinkDto> reorderPois(Integer routeId, ReorderRoutePoiRequest body) {
    ensureRoute(routeId);
    if (body == null || body.orderedIds() == null) return listPois(routeId);
    AtomicInteger pos = new AtomicInteger(0);
    for (Integer id : body.orderedIds()) {
      ripRepo.findOneForRoute(routeId, id).ifPresent(r -> { r.setPosition(pos.getAndIncrement()); });
    }
    return listPois(routeId);
  }

  // --- Lodgings (links) ---

  public java.util.List<AdminRouteLodgingLinkDto> listLodgings(Integer routeId) {
    ensureRoute(routeId);
    return rlRepo.listByRoute(routeId).stream().map(Mappers::toAdminRouteLodgingLink).collect(Collectors.toList());
  }

  public AdminRouteLodgingLinkDto addLodging(Integer routeId, CreateRouteLodgingLinkRequest r) {
    var route = ensureRoute(routeId);
    var lodging = lodgingRepo.findById(r.lodgingId()).orElseThrow(() -> new NoSuchElementException("Lodging not found"));
    var link = new RouteLodging();
    link.setRoute(route);
    link.setLodging(lodging);
    return Mappers.toAdminRouteLodgingLink(rlRepo.save(link));
  }

  public void deleteLodging(Integer routeId, Integer routeLodgingId) {
    var link = rlRepo.findOneForRoute(routeId, routeLodgingId)
        .orElseThrow(() -> new NoSuchElementException("Link not found"));
    rlRepo.delete(link);
  }

  private Route ensureRoute(Integer id) {
    return routeRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Route not found"));
  }
}