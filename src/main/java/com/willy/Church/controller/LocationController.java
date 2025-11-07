package com.willy.Church.controller;


import com.willy.Church.model.Location;
import com.willy.Church.model.enums.LocationType;
import com.willy.Church.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> one(@PathVariable Long id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        Location saved = locationService.save(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id, @RequestBody Location location) {
        location.setId(id);
        return ResponseEntity.ok(locationService.save(location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<Location>> provinces() {
        return ResponseEntity.ok(locationService.findProvinces());
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Location>> children(@PathVariable Long parentId) {
        return ResponseEntity.ok(locationService.findChildren(parentId));
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Location>> byType(@PathVariable LocationType type) {
        return ResponseEntity.ok(locationService.findByType(type));
    }

    @GetMapping(params = "q")
    public ResponseEntity<Page<Location>> search(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(locationService.search(q, pageable));
    }

    @GetMapping(params = "code")
    public ResponseEntity<Location> byCode(@RequestParam String code) {
        Optional<Location> province = locationService.findByCode(code);
        return province.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{parentId}/count-children")
    public ResponseEntity<Long> countChildren(@PathVariable Long parentId) {
        return ResponseEntity.ok(locationService.countChildren(parentId));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam String name,
            @RequestParam LocationType type) {
        return ResponseEntity.ok(locationService.existsByNameAndType(name, type));
    }

    @GetMapping("/tree")
    public ResponseEntity<List<Location>> tree() {
        return ResponseEntity.ok(locationService.findProvincesWithChildren());
    }


    @GetMapping("/exists-under-parent")
    public ResponseEntity<Boolean> existsByNameAndParent(
            @RequestParam String name,
            @RequestParam Long parentId) {
        Location parent = locationService.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent location not found"));
        return ResponseEntity.ok(locationService.existsByNameAndParent(name, parent));
    }

    @GetMapping(params = {"name", "type"})
    public ResponseEntity<Location> findByNameAndType(
            @RequestParam String name,
            @RequestParam LocationType type) {
        return locationService.findByNameAndType(name, type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}