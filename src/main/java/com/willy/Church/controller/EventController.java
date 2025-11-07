package com.willy.Church.controller;


import com.willy.Church.dto.EventCreateDto;
import com.willy.Church.dto.EventUpdateDto;
import com.willy.Church.model.Event;
import com.willy.Church.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> one(@PathVariable Long id) {
        return eventService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody EventCreateDto dto) {
        Event saved = eventService.createEvent(
                dto.getTitle(),
                dto.getDescription(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getLocationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id,
                                        @RequestBody EventUpdateDto dto) {
        Event updated = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<Page<Event>> all(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(eventService.findAll(pageable));
    }

    @GetMapping(params = "title")
    public ResponseEntity<Optional<Event>> byTitle(@RequestParam String title) {
        return ResponseEntity.ok(eventService.findByTitle(title));
    }

    @GetMapping(params = "locationId")
    public ResponseEntity<List<Event>> byLocation(@RequestParam Long locationId) {
        return ResponseEntity.ok(eventService.findByLocation(locationId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<Event>> upcoming(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(eventService.findUpcoming(pageable));
    }

    @GetMapping("/past")
    public ResponseEntity<Page<Event>> past(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(eventService.findPast(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Event>> search(@RequestParam String q,
                                              @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(eventService.searchByTitle(q, pageable));
    }

    @GetMapping("/between")
    public ResponseEntity<Page<Event>> between(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(eventService.findBetween(from, to, pageable));
    }


    @GetMapping("/count-by-location")
    public ResponseEntity<Long> countByLocation(@RequestParam Long locationId) {
        return ResponseEntity.ok(eventService.countByLocation(locationId));
    }





}