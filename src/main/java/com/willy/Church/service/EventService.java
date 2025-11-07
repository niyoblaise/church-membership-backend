package com.willy.Church.service;
import com.willy.Church.dto.EventUpdateDto;
import com.willy.Church.model.Event;
import com.willy.Church.model.Location;
import com.willy.Church.repository.EventRepository;
import com.willy.Church.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Transactional
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Transactional
    public Event createEvent(String title, String description, LocalDateTime start, LocalDateTime end, Long locationId) {
        if (eventRepository.existsByTitleAndStartTime(title, start)) {
            throw new IllegalStateException("An event with the same title and start time already exists");
        }
        Location loc = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setStartTime(start);
        event.setEndTime(end);
        event.setLocation(loc);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long eventId, EventUpdateDto dto) {
        Event e = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        boolean titleChanged   = dto.getTitle()   != null && !e.getTitle().equals(dto.getTitle());
        boolean startChanged   = dto.getStartTime() != null && !e.getStartTime().equals(dto.getStartTime());
        if ((titleChanged || startChanged) &&
                eventRepository.existsByTitleAndStartTime(
                        dto.getTitle() != null ? dto.getTitle() : e.getTitle(),
                        dto.getStartTime() != null ? dto.getStartTime() : e.getStartTime())) {
            throw new IllegalStateException("Duplicate event title and start time");
        }

        if (dto.getTitle() != null) e.setTitle(dto.getTitle());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getStartTime() != null) e.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) e.setEndTime(dto.getEndTime());
        if (dto.getLocationId() != null) {
            if (!locationRepository.existsById(dto.getLocationId())) {
                throw new IllegalArgumentException("Location id=" + dto.getLocationId() + " not found");
            }
            e.setLocation(locationRepository.getReferenceById(dto.getLocationId()));
        }

        return eventRepository.save(e);
    }

    @Transactional(readOnly = true)
    public Optional<Event> findByTitle(String title) {
        return eventRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Event> findByLocation(Long locationId) {
        return eventRepository.findByLocationId(locationId);
    }

    @Transactional(readOnly = true)
    public Page<Event> findBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return eventRepository.findByStartTimeBetween(start, end, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Event> findUpcoming(Pageable pageable) {
        return eventRepository.findByStartTimeAfter(LocalDateTime.now(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Event> findPast(Pageable pageable) {
        return eventRepository.findByEndTimeBefore(LocalDateTime.now(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Event> searchByTitle(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public long countByLocation(Long locationId) {
        return eventRepository.countByLocationId(locationId);
    }
}