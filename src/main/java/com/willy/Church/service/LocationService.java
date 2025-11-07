package com.willy.Church.service;


import com.willy.Church.model.Location;
import com.willy.Church.model.enums.LocationType;
import com.willy.Church.repository.EventRepository;
import com.willy.Church.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    public LocationService(LocationRepository locationRepository, EventRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        if (eventRepository.existsByLocationId(id)) {
            throw new IllegalStateException("Location has events – cannot delete");
        }
        locationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Location> findProvinces() {
        return locationRepository.findByType(LocationType.PROVINCE);
    }

    @Transactional(readOnly = true)
    public List<Location> findChildren(Long parentId) {
        Location parent = locationRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent location not found"));
        return locationRepository.findByParent(parent);
    }

    @Transactional(readOnly = true)
    public Page<Location> search(String keyword, Pageable pageable) {
        return locationRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findByNameAndType(String name, LocationType type) {
        return locationRepository.findByNameAndType(name, type);
    }

    @Transactional(readOnly = true)
    public List<Location> findByType(LocationType type) {
        return locationRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public boolean existsByNameAndParent(String name, Location parent) {
        return locationRepository.existsByNameAndParent(name, parent);
    }

    @Transactional(readOnly = true)
    public long countChildren(Long parentId) {
        Location parent = locationRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent location not found"));
        return locationRepository.countByParent(parent);
    }

    @Transactional(readOnly = true)
    public boolean existsByNameAndType(String name, LocationType type) {
        return locationRepository.existsByNameAndType(name, type);
    }

    @Transactional(readOnly = true)
    public List<Location> findProvincesWithChildren() {
        return locationRepository.findProvincesWithChildren();
    }
    @Transactional(readOnly = true)
    public Optional<Location> findByCode(String code) {
        return locationRepository.findByCode(code);
    }
}